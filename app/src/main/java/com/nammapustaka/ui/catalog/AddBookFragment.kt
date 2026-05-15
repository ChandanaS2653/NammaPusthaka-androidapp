package com.nammapustaka.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nammapustaka.data.db.AppDatabase
import com.nammapustaka.data.model.Book
import com.nammapustaka.data.repository.BookRepository
import com.nammapustaka.databinding.FragmentAddBookBinding
import java.util.UUID

class AddBookFragment : Fragment() {

    private var _binding: FragmentAddBookBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModels {
        CatalogViewModel.Factory(BookRepository(AppDatabase.getDatabase(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveBook.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val author = binding.etAuthor.text.toString().trim()
            val pages = binding.etPages.text.toString().toIntOrNull() ?: 0

            if (title.isEmpty() || author.isEmpty() || pages == 0) {
                Toast.makeText(requireContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = when (binding.rgCategory.checkedRadioButtonId) {
                binding.rbScience.id -> "Science"
                binding.rbHistory.id -> "History"
                else -> "Story"
            }

            val coverColors = listOf("#FF7043", "#42A5F5", "#66BB6A", "#AB47BC", "#FFA726", "#26C6DA", "#EC407A")
            val randomColor = coverColors.random()

            val book = Book(
                title = title,
                author = author,
                category = category,
                totalPages = pages,
                coverColor = randomColor,
                qrCode = "QR-${UUID.randomUUID().toString().take(6).uppercase()}"
            )

            viewModel.addBook(book)
            Toast.makeText(requireContext(), "\"$title\" added to library!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
