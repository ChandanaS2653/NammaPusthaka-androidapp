package com.nammapustaka.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithDetails(
    @Embedded val transaction: BorrowTransaction,
    @Relation(parentColumn = "bookId", entityColumn = "id")
    val book: Book,
    @Relation(parentColumn = "studentId", entityColumn = "id")
    val student: Student
)
