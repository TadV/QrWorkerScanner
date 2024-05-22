package com.edurda77.qrworker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.edurda77.qrworker.domain.utils.ID
import com.edurda77.qrworker.domain.utils.TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface DbDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCode(techOperationEntity: TechOperationEntity)

    @Query("SELECT * FROM $TABLE")
    fun getAllCodes(): Flow<List<TechOperationEntity>>

    @Query("DELETE FROM $TABLE WHERE ID=:id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM $TABLE WHERE $ID =:id")
    suspend fun getCodeById(
        id: Int,
    ): TechOperationEntity?

    @Query("SELECT * FROM $TABLE WHERE IS_UPLOAD=0")
    suspend fun getCodeByNotUpload(): List<TechOperationEntity>

    @Query("UPDATE $TABLE SET IS_UPLOAD = 1 WHERE IS_UPLOAD=0")
    suspend fun updateUpload()
}