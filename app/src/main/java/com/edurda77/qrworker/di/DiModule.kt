package com.edurda77.qrworker.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {

  /*  @Binds
    @Singleton
    abstract fun bindRepository(scannerRepositoryImpl: ScannerRepositoryImpl): ScannerRepository*/
}