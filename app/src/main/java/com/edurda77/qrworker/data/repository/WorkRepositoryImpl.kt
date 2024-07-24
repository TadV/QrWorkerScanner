package com.edurda77.qrworker.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.edurda77.qrworker.data.local.CodeDatabase
import com.edurda77.qrworker.data.local.TechOperationEntity
import com.edurda77.qrworker.data.mapper.convertToCodesDto
import com.edurda77.qrworker.data.mapper.convertToLocalTechOperation
import com.edurda77.qrworker.data.mapper.convertToLocalTechOperations
import com.edurda77.qrworker.data.mapper.convertToLocalUser
import com.edurda77.qrworker.data.mapper.convertToTechOperations
import com.edurda77.qrworker.data.mapper.convertToUpdateTechOperationBody
import com.edurda77.qrworker.data.remote.ApiServer
import com.edurda77.qrworker.data.remote.RemoteWorkerDto
import com.edurda77.qrworker.domain.model.LocalTechOperation
import com.edurda77.qrworker.domain.model.LocalUser
import com.edurda77.qrworker.domain.model.TechOperation
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

    override suspend fun getAllLocalOperations(): Flow<Resource<List<LocalTechOperation>>> {
        return flow {
            try {
                dao.getAllCodes().collect {operations->
                    emit(Resource.Success(operations.convertToLocalTechOperations()))
                }
            } catch (e: Exception) {
                emit (Resource.Error(message = e.message ?: UNKNOWN_ERROR))
            }
        }
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

    override suspend fun getWorkerFIO(workerCode: String): Resource<RemoteWorkerDto> {
        return try {
            Log.d("getWorkerFIO", "workerCode $workerCode")
            val result = apiServer.getWorkerFio(workerCode)
            Log.d("getWorkerFIO", "result remote $result")
            Resource.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("getWorkerFIO", "error: $e")
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
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
        currentUserCode: String,
        currentUserName: String,
    ): Resource<List<TechOperation>> {
        return try {
            val result = apiServer.getTechOperations(numberOPZS)
            Log.d("getTechOperations", "Result: $result")
            Resource.Success(result.convertToTechOperations(currentUserCode, currentUserName))
        } catch (e: Exception) {
            Log.d("getTechOperations", "Error: $e")
//            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
            Resource.Error(message = "ОПЗС не найдена")
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

    override suspend fun getCurrentUser(): Resource<LocalUser?> {
        return try {
            val result = dao.getCurrentUser()
            Resource.Success(result.convertToLocalUser())
        } catch (e: Exception) {
            Log.d("getCurrentUser", "error $e")
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun saveCurrentUser(user: LocalUser) {
        dao.disableUsers()
        dao.addUser(user.userCode, user.userName, 1)
    }

    override suspend fun logOut() {
        dao.disableUsers()
    }
}