package com.nammapustaka.ui.borrow

import androidx.lifecycle.*
import com.nammapustaka.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class BorrowViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _borrowResult = MutableLiveData<String>()
    val borrowResult: LiveData<String> = _borrowResult

    fun borrowBook(qrCode: String, studentId: Int) {
        viewModelScope.launch {
            val book = repository.getBookByQrCode(qrCode)
            if (book == null) {
                _borrowResult.postValue("No book found for QR: $qrCode")
                return@launch
            }
            val success = repository.borrowBook(book.id, studentId)
            _borrowResult.postValue(
                if (success) "\"${book.title}\" issued to student #$studentId successfully!"
                else "\"${book.title}\" is already borrowed by someone."
            )
        }
    }

    class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return BorrowViewModel(repository) as T
        }
    }
}
