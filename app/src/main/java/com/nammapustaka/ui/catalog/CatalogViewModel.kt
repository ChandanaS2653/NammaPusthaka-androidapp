package com.nammapustaka.ui.catalog

import androidx.lifecycle.*
import com.nammapustaka.data.model.Book
import com.nammapustaka.data.repository.BookRepository
import kotlinx.coroutines.launch

class CatalogViewModel(private val repository: BookRepository) : ViewModel() {

    private data class Filter(val category: String = "All", val query: String = "")

    private val _filter = MutableLiveData(Filter())

    val books: LiveData<List<Book>> = _filter.switchMap { filter ->
        when {
            filter.query.isNotEmpty() -> repository.searchBooks(filter.query)
            filter.category == "All" -> repository.getAllBooks()
            else -> repository.getBooksByCategory(filter.category)
        }
    }

    fun filterByCategory(category: String) {
        _filter.value = _filter.value?.copy(category = category, query = "")
    }

    fun search(query: String) {
        _filter.value = _filter.value?.copy(query = query)
    }

    fun addBook(book: Book) {
        viewModelScope.launch { repository.insertBook(book) }
    }

    class Factory(private val repository: BookRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CatalogViewModel(repository) as T
        }
    }
}
