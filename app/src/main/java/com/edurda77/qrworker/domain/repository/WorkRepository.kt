package com.edurda77.qrworker.domain.repository

import com.edurda77.qrworker.domain.model.LocalTechOperation
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface WorkRepository {
    suspend fun insertQrCode(
        id: Int,
        user: String,
        timeScan: String,
        techOperation: String,
        productionReport: String
    )

    suspend fun deleteCodeById(id: Int)
    suspend fun uploadPerDayData()
    suspend fun getAllRemoteCode()
    suspend fun forceUploadData()
    suspend fun getTechOperations(
        numberOPZS: String,
        codeUser: String
    ): Resource<List<TechOperation>>

    suspend fun updateTechOperations(techOperations: List<TechOperation>): Resource<Boolean>
    suspend fun getCodeById(id: Int): Resource<LocalTechOperation?>
    suspend fun getAllLocalOperations(): Flow<Resource<List<LocalTechOperation>>>
}