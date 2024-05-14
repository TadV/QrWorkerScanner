package com.edurda77.qrworker.di


import com.edurda77.qrworker.data.repository.SharedRepositoryImpl
import com.edurda77.qrworker.data.repository.WorkRepositoryImpl
import com.edurda77.qrworker.domain.repository.SharedRepository
import com.edurda77.qrworker.domain.repository.WorkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {

    @Binds
    @Singleton
    abstract fun bindRepository(workRepositoryImpl: WorkRepositoryImpl): WorkRepository

    @Binds
    @Singleton
    abstract fun bindSharedRepository(sharedRepositoryImpl: SharedRepositoryImpl): SharedRepository
}