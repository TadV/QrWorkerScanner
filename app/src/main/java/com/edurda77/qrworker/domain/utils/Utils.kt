package com.edurda77.qrworker.domain.utils

import android.annotation.SuppressLint
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

@SuppressLint("SimpleDateFormat")
fun String.selectDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("dd MMMM yyyy")
    val date = inputFormat.parse(this.substring(0, 10))
    return if (date!=null) outputFormat.format(date) else ""
}


