package com.edurda77.qrworker.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.edurda77.qrworker.data.local.CodeDatabase
import com.edurda77.qrworker.data.local.TechOperationEntity
import com.edurda77.qrworker.data.mapper.convertToCodesDto
import com.edurda77.qrworker.data.mapper.convertToLocalTechOperation
import com.edurda77.qrworker.data.mapper.convertToTechOperations
import com.edurda77.qrworker.data.mapper.convertToUpdateTechOperationBody
import com.edurda77.qrworker.data.remote.ApiServer
import com.edurda77.qrworker.domain.model.LocalTechOperation
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.domain.repository.WorkRepository
import com.edurda77.qrworker.domain.utils.Resource
import com.edurda77.qrworker.domain.utils.SHARED_DATA
import com.edurda77.qrworker.domain.utils.SHARED_DATE
import com.edurda77.qrworker.domain.utils.UNKNOWN_ERROR
import com.edurda77.qrworker.domain.utils.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkRepositoryImpl @Inject constructor(
    application: Application,
    private val apiServer: ApiServer,
    db: CodeDatabase
) : WorkRepository {
    private val dao = db.dbDao
    private val sharedPref = application.getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE)


    override suspend fun insertQrCode(
        id: Int,
        user: String,
        timeScan: String,
        techOperation: String,
        productionReport: String
    ) {
        dao.insertCode(
            TechOperationEntity(
                id = id,
                codeUser = user,
                timeOfChoose = timeScan,
                techOperation = techOperation,
                productionReport = productionReport
            )
        )
    }


    override suspend fun deleteCodeById(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun getCodeById(id: Int): Resource<LocalTechOperation?> {
        return try {
            val result = dao.getCodeById(id)
            Resource.Success(result?.convertToLocalTechOperation())
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
        }
    }


    override suspend fun uploadPerDayData() {
        val currentDate = getCurrentDate()
        withContext(Dispatchers.IO) {
            if (getSavedDate() != currentDate) {
                uploadData(currentDate)
            }
        }
    }

    override suspend fun forceUploadData() {
        val currentDate = getCurrentDate()
        withContext(Dispatchers.IO) {
            uploadData(currentDate)
        }
    }

    override suspend fun getAllRemoteCode() {
        try {
            val result = apiServer.getRemoteCodes()
            Log.d("test connect", "result remote $result")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("test connect", "error: $e")
        }
    }

    private suspend fun uploadData(currentDate: String) {
        try {
            val notUploadedCodes = dao.getCodeByNotUpload().convertToCodesDto()
            if (notUploadedCodes.isNotEmpty()) {
                try {
                    val resultUpload = apiServer.uploadCodes(notUploadedCodes)
                    Log.d("test connect", "result remote $resultUpload")
                    if (resultUpload.code == 200) {
                        dao.updateUpload()
                        saveDate(currentDate)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun getTechOperations(
        numberOPZS: String,
        codeUser: String,
    ): Resource<List<TechOperation>> {
        return try {
            val result = apiServer.getTechOperations(numberOPZS)
            Log.d("Test repository", "result tech opert $result")
            Resource.Success(result.convertToTechOperations(codeUser))
        } catch (e: Exception) {
            Log.d("Test repository", "error $e")
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun updateTechOperations(
        techOperations: List<TechOperation>
    ): Resource<Boolean> {
        return try {
            Log.d("Test repository before send", "techOperations ${techOperations.convertToUpdateTechOperationBody()}")
            val result = apiServer.updateTechOperations(
                techOperations = techOperations.convertToUpdateTechOperationBody()
            )
            Log.d("Test repository", "result $result")
            Resource.Success(result.remoteData)
        } catch (e: Exception) {
            Log.d("Test repository", "error $e")
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
        }
    }


    private fun getSavedDate() = sharedPref.getString(SHARED_DATE, "")

    private fun saveDate(date: String) =
        sharedPref.edit().putString(SHARED_DATE, date).apply()
}