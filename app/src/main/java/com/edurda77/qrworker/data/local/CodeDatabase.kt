package com.edurda77.qrworker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TechOperationEntity::class],
    version = 1
)
abstract class CodeDatabase: RoomDatabase() {
    abstract val dbDao:DbDao
}