package com.edurda77.qrworker.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.edurda77.qrworker.data.local.CodeDatabase
import com.edurda77.qrworker.data.local.CodeEntity
import com.edurda77.qrworker.data.mapper.convertToCodesDto
import com.edurda77.qrworker.data.mapper.convertToQrCode
import com.edurda77.qrworker.data.mapper.convertToQrCodes
import com.edurda77.qrworker.data.remote.ApiServer
import com.edurda77.qrworker.domain.model.QrCode
import com.edurda77.qrworker.domain.repository.WorkRepository
import com.edurda77.qrworker.domain.utils.Resource
import com.edurda77.qrworker.domain.utils.SHARED_DATA
import com.edurda77.qrworker.domain.utils.SHARED_DATE
import com.edurda77.qrworker.domain.utils.UNKNOWN_ERROR
import com.edurda77.qrworker.domain.utils.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkRepositoryImpl @Inject constructor(
    application: Application,
    private val apiServer: ApiServer,
    db: CodeDatabase
) : WorkRepository {
    private val dao = db.dbDao
    private val sharedPref = application.getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE)

    override suspend fun getAllQrCodes(): Flow<Resource<List<QrCode>>> {
        return flow {
            try {
                val result = dao.getAllCodes()
                result.collect { codes ->
                    emit(Resource.Success(data = codes.convertToQrCodes()))
                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: UNKNOWN_ERROR))
            }
        }
    }

    override suspend fun getQrCode(qrCode: String): Resource<QrCode?> {
        return try {
            val items = qrCode.split("/".toRegex())
            val result = dao.getCodeByCodeQr(
                techOperation = items[0],
                productionReport = if (items.size>1) items[1] else items[0]
            )
            Resource.Success(data = result?.convertToQrCode())
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun insertQrCode(
        user: String,
        timeScan: String,
        qrCode: String
    ) {
        val items = qrCode.split("/".toRegex())
        dao.insertCode(
            CodeEntity(
                codeUser = user,
                timeOfScan = timeScan,
                techOperation = items[0],
                productionReport = if (items.size>1) items[1] else items[0]
            )
        )
    }

    override suspend fun clearQrCodes() {
        dao.clear()
    }


    override suspend fun uploadPerDayData() {
        val currentDate = getCurrentDate()
        withContext(Dispatchers.IO){
            if (getSavedDate() != currentDate) {
               uploadData(currentDate)
            }
        }
    }

    override suspend fun forceUploadData() {
        val currentDate = getCurrentDate()
        withContext(Dispatchers.IO){
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

    private suspend fun uploadData(currentDate:String) {
        try {
            val notUploadedCodes = dao.getCodeByNotUpload().convertToCodesDto()
            if (notUploadedCodes.isNotEmpty()) {
                try {
                    val resultUpload = apiServer.uploadCodes(notUploadedCodes)
                    Log.d("test connect", "result remote $resultUpload")
                    if (resultUpload.code==200) {
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

    private fun getSavedDate() = sharedPref.getString(SHARED_DATE, "")

    private fun saveDate(date: String) =
        sharedPref.edit().putString(SHARED_DATE, date).apply()
}