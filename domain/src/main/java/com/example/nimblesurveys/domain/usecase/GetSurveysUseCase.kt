package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.exception.UnauthorizedException
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.repository.AuthRepository
import com.example.nimblesurveys.domain.repository.SurveyRepository

class GetSurveysUseCase(
    private val repository: SurveyRepository,
    private val authRepository: AuthRepository
) {

    suspend fun execute(): Result<List<Survey>> {
        var result = try {
            getSurveysResult()
        } catch (e: Throwable) {
            Result.Failure(e)
        }

        if (result is Result.Failure && result.cause is UnauthorizedException) {
            result = try {
                authRepository.refreshAccessToken()
                getSurveysResult()
            } catch (e: Throwable) {
                Result.Failure(e)
            }
        }

        return result
    }

    private suspend fun getSurveysResult(): Result<List<Survey>> {
        val surveys = repository.getSurveys()
        return Result.Success(surveys)
    }
}