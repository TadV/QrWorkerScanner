package com.edurda77.qrworker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.edurda77.qrworker.domain.utils.GUID_PRODUCTION_REPORT
import com.edurda77.qrworker.domain.utils.GUID_TECH_OPERATION
import com.edurda77.qrworker.domain.utils.TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface DbDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCode(codeEntity: CodeEntity)

    @Query("SELECT * FROM $TABLE")
    fun getAllCodes(): Flow<List<CodeEntity>>

    @Query("DELETE FROM $TABLE")
    suspend fun clear()

    @Query("SELECT * FROM $TABLE WHERE $GUID_TECH_OPERATION =:techOperation AND $GUID_PRODUCTION_REPORT =:productionReport")
    suspend fun getCodeByCodeQr(
        techOperation: String,
        productionReport: String
    ): CodeEntity?

    @Query("SELECT * FROM $TABLE WHERE IS_UPLOAD=0")
    suspend fun getCodeByNotUpload(): List<CodeEntity>

    @Query("UPDATE $TABLE SET IS_UPLOAD = 1 WHERE IS_UPLOAD=0")
    suspend fun updateUpload()
}