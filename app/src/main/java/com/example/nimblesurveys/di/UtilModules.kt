package com.example.nimblesurveys.di

import com.example.nimblesurveys.data.repository.TimeProviderImpl
import com.example.nimblesurveys.domain.provider.DispatcherProvider
import com.example.nimblesurveys.domain.provider.TimeProvider
import com.example.nimblesurveys.util.DispatcherProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TimeModule {

    @Provides
    fun provideTimeProvider(): TimeProvider = TimeProviderImpl()
}

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProviderImpl()
}