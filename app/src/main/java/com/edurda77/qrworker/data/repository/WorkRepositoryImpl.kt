package com.edurda77.qrworker.data.repository

import android.app.Application
import android.content.Context
import android.os.Build
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
import com.edurda77.qrworker.domain.utils.getCurrentTime
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
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


    override suspend fun uploadData() {
        val currentDate = getCurrentDate()
        withContext(Dispatchers.IO){
            if (getSavedDate() != currentDate) {
                try {
                    val notUploadedCodes = dao.getCodeByNotUpload().convertToCodesDto()
                    if (notUploadedCodes.isNotEmpty()) {
                        try {
                            val resultUpload = apiServer.uploadCodes(notUploadedCodes)
                            if (resultUpload.isSuccessful) {
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
        }
    }

    override suspend fun uploadDataAsFile() {
        val currentDate = getCurrentDate()
        withContext(Dispatchers.IO){
            if (getSavedDate() != currentDate) {
                try {
                    val notUploadedCodes = dao.getCodeByNotUpload().convertToCodesDto()
                    if (notUploadedCodes.isNotEmpty()) {
                        try {
                            val nameFile = "${getCurrentTime()}_${notUploadedCodes.first().codeUser}.json"
                            val file = File(nameFile)
                            notUploadedCodes.map {
                                val gson = Gson()
                                val jsonString = gson.toJson(it)
                                file.appendText(jsonString)
                            }
                            val targetDirectory = "/path/to/target/directory"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val targetPath = Paths.get(targetDirectory, nameFile)
                                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING)
                            } else {
                                val targetFile = File(targetDirectory, nameFile)
                                val inputStream = FileInputStream(file)
                                val outputStream = FileOutputStream(targetFile)
                                val buffer = ByteArray(1024)
                                var length: Int

                                while (inputStream.read(buffer).also { length = it } > 0) {
                                    outputStream.write(buffer, 0, length)
                                }
                                inputStream.close()
                                outputStream.close()
                            }
                            dao.updateUpload()
                            saveDate(currentDate)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getSavedDate() = sharedPref.getString(SHARED_DATE, "")

    private fun saveDate(date: String) =
        sharedPref.edit().putString(SHARED_DATE, date).apply()
}