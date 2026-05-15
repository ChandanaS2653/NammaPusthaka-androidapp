package com.nammapustaka.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val category: String,
    val totalPages: Int,
    val coverColor: String = "#4CAF50",
    val qrCode: String,
    val isAvailable: Boolean = true
)
