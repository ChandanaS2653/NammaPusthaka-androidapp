package com.nammapustaka.ui.leaderboard

import androidx.lifecycle.*
import com.nammapustaka.data.db.AppDatabase

class LeaderboardViewModel(db: AppDatabase) : ViewModel() {

    val rankedStudents = db.studentDao().getAllStudentsRanked()

    class Factory(private val db: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardViewModel(db) as T
        }
    }
}
