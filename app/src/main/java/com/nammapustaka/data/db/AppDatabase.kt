package com.nammapustaka.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nammapustaka.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Book::class, Student::class, BorrowTransaction::class, BookReview::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun studentDao(): StudentDao
    abstract fun transactionDao(): TransactionDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_pustaka_db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                seedData(database)
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedData(db: AppDatabase) {
            db.studentDao().insertStudent(Student(name = "Arjun Kumar", className = "Class 5A"))
            db.studentDao().insertStudent(Student(name = "Priya Sharma", className = "Class 6B"))
            db.studentDao().insertStudent(Student(name = "Rahul Nair", className = "Class 5B"))
            db.studentDao().insertStudent(Student(name = "Meena Devi", className = "Class 4A"))

            db.bookDao().insertBook(Book(title = "Panchatantra Tales", author = "Vishnu Sharma", category = "Story", totalPages = 120, coverColor = "#FF7043", qrCode = "QR001"))
            db.bookDao().insertBook(Book(title = "Ramayana for Children", author = "Valmiki", category = "Story", totalPages = 200, coverColor = "#42A5F5", qrCode = "QR002"))
            db.bookDao().insertBook(Book(title = "Science Wonders", author = "APJ Abdul Kalam", category = "Science", totalPages = 150, coverColor = "#66BB6A", qrCode = "QR003"))
            db.bookDao().insertBook(Book(title = "History of Karnataka", author = "R. Shejwalkar", category = "History", totalPages = 180, coverColor = "#AB47BC", qrCode = "QR004"))
            db.bookDao().insertBook(Book(title = "Math Magic", author = "Shakuntala Devi", category = "Science", totalPages = 90, coverColor = "#FFA726", qrCode = "QR005"))
            db.bookDao().insertBook(Book(title = "Tenali Ramakrishna Stories", author = "Traditional", category = "Story", totalPages = 110, coverColor = "#26C6DA", qrCode = "QR006"))
            db.bookDao().insertBook(Book(title = "Our Solar System", author = "ISRO Publications", category = "Science", totalPages = 130, coverColor = "#EC407A", qrCode = "QR007"))
        }
    }
}
