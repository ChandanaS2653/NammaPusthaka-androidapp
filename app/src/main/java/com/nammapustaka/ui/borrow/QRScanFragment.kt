package com.nammapustaka.ui.borrow

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.nammapustaka.data.db.AppDatabase
import com.nammapustaka.data.repository.TransactionRepository
import com.nammapustaka.databinding.FragmentQrScanBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QRScanFragment : Fragment() {

    private var _binding: FragmentQrScanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BorrowViewModel by viewModels {
        BorrowViewModel.Factory(TransactionRepository(AppDatabase.getDatabase(requireContext())))
    }

    private lateinit var cameraExecutor: ExecutorService
    private var scanHandled = false

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera()
            else Toast.makeText(requireContext(), "Camera permission required for QR scan", Toast.LENGTH_SHORT).show()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Start camera for QR scanning
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) startCamera()
        else requestCameraPermission.launch(Manifest.permission.CAMERA)

        // Observe borrow result
        viewModel.borrowResult.observe(viewLifecycleOwner) { result ->
            Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
            resetScan()
        }

        // Manual QR lookup button
        binding.btnLookup.setOnClickListener {
            val qrCode = binding.etManualQR.text.toString().trim().uppercase()
            if (qrCode.isEmpty()) {
                Toast.makeText(requireContext(), "Enter a QR code (e.g. QR001)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showBookResult(qrCode)
        }

        // Issue book button
        binding.btnIssueBook.setOnClickListener {
            val qrCode = binding.tvScannedCode.tag as? String ?: return@setOnClickListener
            val studentId = binding.etStudentId.text.toString().toIntOrNull()
            if (studentId == null) {
                Toast.makeText(requireContext(), "Enter a valid Student ID (1-4)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.borrowBook(qrCode, studentId)
        }

        // Scan again button
        binding.btnScanAgain.setOnClickListener {
            resetScan()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_ALL_FORMATS)
                .build()

            val analyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QRCodeAnalyzer(options) { qrCode ->
                        if (!scanHandled) {
                            scanHandled = true
                            requireActivity().runOnUiThread { showBookResult(qrCode) }
                        }
                    })
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analyzer
                )
            } catch (e: Exception) {
                binding.tvScanStatus.text = "Camera unavailable — use manual entry below"
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun showBookResult(qrCode: String) {
        binding.layoutResult.visibility = View.VISIBLE
        binding.tvScannedCode.text = "Book QR: $qrCode"
        binding.tvScannedCode.tag = qrCode
        binding.tvScanStatus.text = "QR code found — enter Student ID to issue"
        binding.etStudentId.text?.clear()
        binding.etManualQR.text?.clear()
    }

    private fun resetScan() {
        scanHandled = false
        binding.layoutResult.visibility = View.GONE
        binding.tvScanStatus.text = "Waiting for QR scan..."
        binding.etStudentId.text?.clear()
        binding.etManualQR.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}

class QRCodeAnalyzer(
    private val options: BarcodeScannerOptions,
    private val onDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient(options)

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run { imageProxy.close(); return }
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull { !it.rawValue.isNullOrEmpty() }
                    ?.rawValue?.let { onDetected(it) }
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}
