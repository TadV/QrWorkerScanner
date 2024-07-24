package com.edurda77.qrworker.di

import android.app.Application
import androidx.room.Room
import com.edurda77.qrworker.data.local.CodeDatabase
import com.edurda77.qrworker.data.remote.ApiServer
import com.edurda77.qrworker.domain.utils.BASE_URL
import com.edurda77.qrworker.domain.utils.DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideApiAnalytic(): ApiServer {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServer::class.java)
    }


}