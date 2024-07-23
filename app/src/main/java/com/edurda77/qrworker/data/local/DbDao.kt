package com.edurda77.qrworker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.edurda77.qrworker.domain.model.LocalUser
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

    @Query("UPDATE users SET active = 0")
    suspend fun disableUsers()
    @Query("UPDATE users SET active = 1 WHERE user_code = :userCode")
    suspend fun setActiveUser(userCode:String)
    @Query("INSERT INTO users (user_code, user_name, active) VALUES (:userCode, :userName, :active)")
    suspend fun addUser(userCode:String, userName:String, active:Int)
    @Query("SELECT * FROM users WHERE active = 1 LIMIT 1")
    suspend fun getCurrentUser(): CurrentUserEntity

}