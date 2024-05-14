package com.edurda77.qrworker.domain.utils

import java.util.Calendar

fun getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    val currentDate = calendar.time.toString()
    return currentDate.replace(" ", "_")
}


