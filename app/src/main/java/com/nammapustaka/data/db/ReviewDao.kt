package com.nammapustaka.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nammapustaka.data.model.BookReview

@Dao
interface ReviewDao {

    @Query("SELECT * FROM reviews WHERE bookId = :bookId ORDER BY id DESC")
    fun getReviewsForBook(bookId: Int): LiveData<List<BookReview>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: BookReview)
}
