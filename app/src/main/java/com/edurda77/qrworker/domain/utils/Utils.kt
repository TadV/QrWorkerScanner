package com.edurda77.qrworker.domain.utils

import android.annotation.SuppressLint
import com.edurda77.qrworker.domain.model.LocalTechOperation
import com.edurda77.qrworker.domain.model.TechOperation
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
fun getCurrentDateTime(): String {
    val calendar = Calendar.getInstance()
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    return dateTimeFormat.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun String.selectDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("dd MMMM yyyy")
    val date = inputFormat.parse(this.substring(0, 10))
    return if (date != null) outputFormat.format(date) else ""
}

fun checkConflicts(
    remoteOperations: List<TechOperation>,
    localOperations: List<LocalTechOperation>
): Boolean {
    localOperations.forEach { localOperation ->
        return remoteOperations.find { it.id == localOperation.id && it.codeUser != localOperation.codeUser } != null
    }
    return false
}

fun checkConflictsOperations(
    remoteOperations: List<TechOperation>,
    localOperations: List<LocalTechOperation>
): List<TechOperation> {
    val conflictOperations = mutableListOf<TechOperation>()
    localOperations.forEach { localOperation ->
        if (remoteOperations.find { it.id == localOperation.id && it.codeUser != localOperation.codeUser } != null) {
            conflictOperations.add(remoteOperations.first { it.id == localOperation.id })
        }
    }
    return conflictOperations
}

fun List<TechOperation>.compareLists(fooApiList: List<String>) = filter { m -> fooApiList.any { m.techOperation.contains(it, ignoreCase = true) } }
fun List<TechOperation>.filterLists(fooApiList: List<String>) = filter { m -> fooApiList.any { m.techOperationName.contains(it, ignoreCase = true) } }


