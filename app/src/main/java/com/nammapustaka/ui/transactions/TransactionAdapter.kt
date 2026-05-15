package com.nammapustaka.ui.transactions

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nammapustaka.data.model.BorrowTransaction
import com.nammapustaka.data.model.TransactionWithDetails
import com.nammapustaka.databinding.ItemTransactionBinding
import com.nammapustaka.utils.DateUtils

class TransactionAdapter(private val onReturn: (BorrowTransaction) -> Unit) :
    ListAdapter<TransactionWithDetails, TransactionAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionWithDetails) {
            binding.tvBookTitle.text = item.book.title
            binding.tvStudentName.text = item.student.name
            binding.tvClass.text = item.student.className
            binding.tvBorrowDate.text = "Borrowed: ${DateUtils.formatDate(item.transaction.borrowDate)}"
            binding.tvDueDate.text = "Due: ${DateUtils.formatDate(item.transaction.dueDate)}"

            val overdue = DateUtils.isOverdue(item.transaction.dueDate)
            binding.tvDueDate.setTextColor(if (overdue) Color.RED else Color.parseColor("#4CAF50"))
            binding.tvOverdueTag.visibility = if (overdue) android.view.View.VISIBLE else android.view.View.GONE

            binding.btnReturn.setOnClickListener { onReturn(item.transaction) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<TransactionWithDetails>() {
            override fun areItemsTheSame(a: TransactionWithDetails, b: TransactionWithDetails) =
                a.transaction.id == b.transaction.id
            override fun areContentsTheSame(a: TransactionWithDetails, b: TransactionWithDetails) =
                a == b
        }
    }
}
