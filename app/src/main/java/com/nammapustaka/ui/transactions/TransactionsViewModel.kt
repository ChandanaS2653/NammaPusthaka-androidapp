package com.nammapustaka.ui.transactions

import androidx.lifecycle.*
import com.nammapustaka.data.model.BorrowTransaction
import com.nammapustaka.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionsViewModel(private val repository: TransactionRepository) : ViewModel() {

    val activeTransactions = repository.getActiveTransactions()

    fun returnBook(transaction: BorrowTransaction) {
        viewModelScope.launch { repository.returnBook(transaction) }
    }

    class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(repository) as T
        }
    }
}
