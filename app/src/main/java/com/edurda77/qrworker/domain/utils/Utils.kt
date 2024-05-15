package com.edurda77.qrworker.domain.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    val currentDate = calendar.time.toString()
    return currentDate.replace(" ", "_")
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = dateFormat.format(calendar.time)
    return currentDate
}


