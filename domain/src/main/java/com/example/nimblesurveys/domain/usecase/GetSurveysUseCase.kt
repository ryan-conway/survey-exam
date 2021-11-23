package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.exception.MissingRefreshTokenException
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.repository.AuthRepository
import com.example.nimblesurveys.domain.repository.SurveyRepository

class GetSurveysUseCase(
    private val repository: SurveyRepository,
    private val authRepository: AuthRepository
) {

    suspend fun execute(): Result<List<Survey>> {
        return try {
            val token = authRepository.getAccessToken() ?: throw MissingRefreshTokenException()
            val surveys = repository.getSurveys(token)
            Result.Success(surveys)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}