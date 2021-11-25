package com.example.nimblesurveys.di

import com.example.nimblesurveys.data.repository.TimeRepositoryImpl
import com.example.nimblesurveys.domain.provider.DispatcherProvider
import com.example.nimblesurveys.domain.repository.TimeRepository
import com.example.nimblesurveys.util.DispatcherProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TimeModule {

    @Provides
    fun provideTimeRepository(): TimeRepository = TimeRepositoryImpl()
}

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProviderImpl()
}