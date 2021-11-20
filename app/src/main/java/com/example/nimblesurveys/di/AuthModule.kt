package com.example.nimblesurveys.di

import com.example.nimblesurveys.data.adapter.TokenAdapter
import com.example.nimblesurveys.data.api.ApiCredential
import com.example.nimblesurveys.data.api.auth.AuthApiService
import com.example.nimblesurveys.data.cache.SurveyDatabase
import com.example.nimblesurveys.data.repository.AuthRepositoryImpl
import com.example.nimblesurveys.domain.repository.AuthRepository
import com.example.nimblesurveys.domain.repository.TimeRepository
import com.example.nimblesurveys.domain.usecase.GetAccessTokenUseCase
import com.example.nimblesurveys.domain.usecase.GetUserUseCase
import com.example.nimblesurveys.domain.usecase.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    //TODO provide real credentials
    @Provides
    fun provideApiCredential() = ApiCredential("", "")

    @Provides
    @Singleton
    fun provideAuthRepository(
        database: SurveyDatabase,
        retrofit: Retrofit,
        apiCredential: ApiCredential,
        timeRepository: TimeRepository
    ): AuthRepository {
        return AuthRepositoryImpl(
            database.authDao(),
            retrofit.create(AuthApiService::class.java),
            apiCredential,
            timeRepository,
            TokenAdapter()
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    @Provides
    fun provideGetAccessTokenUseCase(repository: AuthRepository) = GetAccessTokenUseCase(repository)

    @Provides
    fun provideGetUserUseCase(repository: AuthRepository) = GetUserUseCase(repository)

    @Provides
    fun provideLoginUseCase(repository: AuthRepository) = LoginUseCase(repository)
}