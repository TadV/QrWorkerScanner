package com.edurda77.qrworker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.edurda77.qrworker.domain.utils.CODE_USER
import com.edurda77.qrworker.domain.utils.GUID_PRODUCTION_REPORT
import com.edurda77.qrworker.domain.utils.GUID_TECH_OPERATION
import com.edurda77.qrworker.domain.utils.ID
import com.edurda77.qrworker.domain.utils.IS_UPLOAD
import com.edurda77.qrworker.domain.utils.TABLE
import com.edurda77.qrworker.domain.utils.TIME_SCAN

@Entity(tableName = "users")
data class CurrentUserEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "user_code")
    val userCode: String,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "active")
    val active: Int,
)