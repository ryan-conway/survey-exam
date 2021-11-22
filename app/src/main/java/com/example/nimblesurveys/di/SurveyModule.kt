package com.example.nimblesurveys.di

import com.example.nimblesurveys.data.adapter.SurveyAdapter
import com.example.nimblesurveys.data.api.survey.SurveyApiService
import com.example.nimblesurveys.data.cache.SurveyDatabase
import com.example.nimblesurveys.data.repository.SurveyRepositoryImpl
import com.example.nimblesurveys.domain.repository.SurveyRepository
import com.example.nimblesurveys.domain.usecase.GetSurveysUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SurveyModule {

    @Provides
    @Singleton
    fun provideSurveyRepository(
        retrofit: Retrofit,
        database: SurveyDatabase
    ): SurveyRepository {
        return SurveyRepositoryImpl(
            retrofit.create(SurveyApiService::class.java),
            database.surveyDao(),
            SurveyAdapter()
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SurveyUseCaseModule {

    @Provides
    fun provideGetSurveysUseCase(repository: SurveyRepository) = GetSurveysUseCase(repository)
}