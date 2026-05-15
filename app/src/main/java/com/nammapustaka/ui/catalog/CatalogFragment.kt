package com.nammapustaka.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.nammapustaka.R
import com.nammapustaka.data.db.AppDatabase
import com.nammapustaka.data.model.Book
import com.nammapustaka.data.repository.BookRepository
import com.nammapustaka.databinding.FragmentCatalogBinding

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModels {
        CatalogViewModel.Factory(BookRepository(AppDatabase.getDatabase(requireContext())))
    }

    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookAdapter = BookAdapter { book -> showBookDialog(book) }

        binding.recyclerBooks.apply {
            adapter = bookAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        viewModel.books.observe(viewLifecycleOwner) { books ->
            bookAdapter.submitList(books)
            binding.tvEmpty.visibility = if (books.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText.orEmpty())
                return true
            }
        })

        setupCategoryChips()

        binding.fabAddBook.setOnClickListener {
            findNavController().navigate(R.id.action_catalog_to_addBook)
        }
    }

    private fun setupCategoryChips() {
        listOf("All", "Story", "Science", "History").forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                isChecked = category == "All"
            }
            chip.setOnClickListener { viewModel.filterByCategory(category) }
            binding.chipGroupCategory.addView(chip)
        }
    }

    private fun showBookDialog(book: Book) {
        AlertDialog.Builder(requireContext())
            .setTitle(book.title)
            .setMessage(
                "Author: ${book.author}\n" +
                "Category: ${book.category}\n" +
                "Pages: ${book.totalPages}\n" +
                "QR Code: ${book.qrCode}\n" +
                "Status: ${if (book.isAvailable) "Available" else "Currently Borrowed"}"
            )
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
