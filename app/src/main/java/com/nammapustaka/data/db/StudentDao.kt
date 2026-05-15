package com.nammapustaka.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nammapustaka.data.model.Student

@Dao
interface StudentDao {

    @Query("SELECT * FROM students ORDER BY totalPagesRead DESC")
    fun getAllStudentsRanked(): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getStudentById(id: Int): Student?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)
}
