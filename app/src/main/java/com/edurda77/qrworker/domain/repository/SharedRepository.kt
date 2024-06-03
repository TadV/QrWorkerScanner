package com.edurda77.qrworker.domain.repository

interface SharedRepository {
    fun getSavedDate(): String?
    fun saveDate(date: String)
}