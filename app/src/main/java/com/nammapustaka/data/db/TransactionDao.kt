package com.nammapustaka.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nammapustaka.data.model.BorrowTransaction
import com.nammapustaka.data.model.TransactionWithDetails

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM transactions WHERE isReturned = 0 ORDER BY dueDate ASC")
    fun getActiveTransactions(): LiveData<List<TransactionWithDetails>>

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY borrowDate DESC")
    fun getAllTransactions(): LiveData<List<TransactionWithDetails>>

    @Query("SELECT * FROM transactions WHERE bookId = :bookId AND isReturned = 0 LIMIT 1")
    suspend fun getActiveTransactionForBook(bookId: Int): BorrowTransaction?

    @Insert
    suspend fun insertTransaction(transaction: BorrowTransaction): Long

    @Update
    suspend fun updateTransaction(transaction: BorrowTransaction)
}
