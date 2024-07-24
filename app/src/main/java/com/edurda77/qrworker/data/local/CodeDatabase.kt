package com.edurda77.qrworker.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [TechOperationEntity::class, CurrentUserEntity::class],
    exportSchema = true,
)
abstract class CodeDatabase: RoomDatabase() {
    abstract val dbDao:DbDao
}