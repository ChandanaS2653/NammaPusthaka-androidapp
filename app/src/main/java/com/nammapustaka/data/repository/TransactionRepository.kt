package com.nammapustaka.data.repository

import androidx.lifecycle.LiveData
import com.nammapustaka.data.db.AppDatabase
import com.nammapustaka.data.model.BorrowTransaction
import com.nammapustaka.data.model.TransactionWithDetails

class TransactionRepository(private val db: AppDatabase) {

    fun getActiveTransactions(): LiveData<List<TransactionWithDetails>> =
        db.transactionDao().getActiveTransactions()

    fun getAllTransactions(): LiveData<List<TransactionWithDetails>> =
        db.transactionDao().getAllTransactions()

    suspend fun getBookByQrCode(qrCode: String) = db.bookDao().getBookByQrCode(qrCode)

    suspend fun borrowBook(bookId: Int, studentId: Int): Boolean {
        val existing = db.transactionDao().getActiveTransactionForBook(bookId)
        if (existing != null) return false
        val book = db.bookDao().getBookById(bookId) ?: return false
        db.transactionDao().insertTransaction(
            BorrowTransaction(bookId = bookId, studentId = studentId)
        )
        db.bookDao().updateBook(book.copy(isAvailable = false))
        return true
    }

    suspend fun returnBook(transaction: BorrowTransaction) {
        val book = db.bookDao().getBookById(transaction.bookId) ?: return
        val student = db.studentDao().getStudentById(transaction.studentId) ?: return
        db.transactionDao().updateTransaction(
            transaction.copy(isReturned = true, returnDate = System.currentTimeMillis())
        )
        db.bookDao().updateBook(book.copy(isAvailable = true))
        db.studentDao().updateStudent(
            student.copy(totalPagesRead = student.totalPagesRead + book.totalPages)
        )
    }
}
