package com.nammapustaka.ui.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nammapustaka.data.model.Student
import com.nammapustaka.databinding.ItemLeaderboardBinding

class LeaderboardAdapter : ListAdapter<Student, LeaderboardAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student, rank: Int) {
            binding.tvRank.text = "#$rank"
            binding.tvStudentName.text = student.name
            binding.tvClass.text = student.className
            binding.tvPages.text = "${student.totalPagesRead} pages"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), position + 1)

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(a: Student, b: Student) = a.id == b.id
            override fun areContentsTheSame(a: Student, b: Student) = a == b
        }
    }
}
