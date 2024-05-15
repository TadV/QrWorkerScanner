package com.edurda77.qrworker.data.repository

import android.app.Application
import android.content.Context
import com.edurda77.qrworker.domain.repository.SharedRepository
import com.edurda77.qrworker.domain.utils.SHARED_DATA
import com.edurda77.qrworker.domain.utils.SHARED_DATE
import javax.inject.Inject

class SharedRepositoryImpl @Inject constructor(
    application: Application
): SharedRepository {
    private val sharedPref = application.getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE)

    override fun getSavedDate() = sharedPref.getString(SHARED_DATE, "")

    override fun saveDate(date: String) =
        sharedPref.edit().putString(SHARED_DATE, date).apply()
}