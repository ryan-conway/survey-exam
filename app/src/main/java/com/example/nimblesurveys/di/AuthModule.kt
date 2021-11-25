package com.example.nimblesurveys.di

import com.example.nimblesurveys.BuildConfig
import com.example.nimblesurveys.data.api.ApiCredential
import com.example.nimblesurveys.data.api.auth.AuthApiService
import com.example.nimblesurveys.data.cache.SurveyDatabase
import com.example.nimblesurveys.data.repository.AuthRepositoryImpl
import com.example.nimblesurveys.domain.provider.TimeProvider
import com.example.nimblesurveys.domain.repository.AuthRepository
import com.example.nimblesurveys.domain.usecase.GetUserUseCase
import com.example.nimblesurveys.domain.usecase.IsLoggedInUseCase
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

    @Provides
    fun provideApiCredential() = ApiCredential(
        key = BuildConfig.API_KEY,
        secret = BuildConfig.API_SECRET
    )

    @Provides
    @Singleton
    fun provideAuthRepository(
        database: SurveyDatabase,
        retrofit: Retrofit,
        apiCredential: ApiCredential,
        timeProvider: TimeProvider
    ): AuthRepository {
        return AuthRepositoryImpl(
            database.authDao(),
            retrofit.create(AuthApiService::class.java),
            apiCredential,
            timeProvider,
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    @Provides
    fun provideGetUserUseCase(repository: AuthRepository) = GetUserUseCase(repository)

    @Provides
    fun provideLoginUseCase(repository: AuthRepository) = LoginUseCase(repository)

    @Provides
    fun provideIsLoggedInUseCase(repository: AuthRepository) = IsLoggedInUseCase(repository)
}