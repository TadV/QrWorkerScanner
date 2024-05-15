package com.edurda77.qrworker.domain.repository

import com.edurda77.qrworker.domain.model.QrCode
import com.edurda77.qrworker.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface WorkRepository {
    suspend fun getAllQrCodes(): Flow<Resource<List<QrCode>>>
    suspend fun getQrCode(qrCode: String): Resource<QrCode?>
    suspend fun insertQrCode(user: String, timeScan: String, qrCode: String)
    suspend fun clearQrCodes()
    suspend fun uploadPerDayData()
    suspend fun getAllRemoteCode()
    suspend fun forceUploadData()
}