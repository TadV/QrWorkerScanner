package com.edurda77.qrworker.data.repository

import android.app.Application
import com.edurda77.qrworker.data.local.CodeDatabase
import com.edurda77.qrworker.data.local.CodeEntity
import com.edurda77.qrworker.data.mapper.convertToQrCode
import com.edurda77.qrworker.data.mapper.convertToQrCodes
import com.edurda77.qrworker.domain.model.QrCode
import com.edurda77.qrworker.domain.repository.WorkRepository
import com.edurda77.qrworker.domain.utils.Resource
import com.edurda77.qrworker.domain.utils.UNKNOWN_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkRepositoryImpl @Inject constructor(
    db: CodeDatabase
) : WorkRepository {
    private val dao = db.dbDao

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

    override suspend fun getQrCode(qrCode:String): Resource<QrCode?> {
        return  try {
            val result = dao.getCodeByCodeQr(qrCode)
            Resource.Success(data = result?.convertToQrCode())
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun insertQrCode(
        user:String,
        timeScan:String,
        qrCode:String) {
        dao.insertCode(
            CodeEntity(
                codeUser = user,
                timeOfScan = timeScan,
                codeQr = qrCode
            )
        )
    }

    override suspend fun clearQrCodes() {
        dao.clear()
    }

}