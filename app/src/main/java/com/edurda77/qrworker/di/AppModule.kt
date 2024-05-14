package com.edurda77.qrworker.di

import android.app.Application
import androidx.room.Room
import com.edurda77.qrworker.data.local.CodeDatabase
import com.edurda77.qrworker.domain.utils.DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): CodeDatabase {
        return Room.databaseBuilder(
            app,
            CodeDatabase::class.java,
            DB
        )
            .build()
    }

}