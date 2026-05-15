package com.nammapustaka.data.repository

import androidx.lifecycle.LiveData
import com.nammapustaka.data.db.AppDatabase
import com.nammapustaka.data.model.Book

class BookRepository(private val db: AppDatabase) {

    fun getAllBooks(): LiveData<List<Book>> = db.bookDao().getAllBooks()

    fun getBooksByCategory(category: String): LiveData<List<Book>> =
        db.bookDao().getBooksByCategory(category)

    fun searchBooks(query: String): LiveData<List<Book>> =
        db.bookDao().searchBooks(query)

    suspend fun getBookByQrCode(qrCode: String): Book? =
        db.bookDao().getBookByQrCode(qrCode)

    suspend fun insertBook(book: Book) = db.bookDao().insertBook(book)

    suspend fun updateBook(book: Book) = db.bookDao().updateBook(book)
}
