package com.edurda77.qrworker.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "users", primaryKeys = ["user_code"])
data class CurrentUserEntity (
    @ColumnInfo(name = "user_code")
    val userCode: String,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "active")
    val active: Int,
)