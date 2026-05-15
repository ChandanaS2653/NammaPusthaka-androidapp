package com.nammapustaka.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun formatDate(timestamp: Long): String = formatter.format(Date(timestamp))

    fun isOverdue(dueDate: Long): Boolean = System.currentTimeMillis() > dueDate
}
