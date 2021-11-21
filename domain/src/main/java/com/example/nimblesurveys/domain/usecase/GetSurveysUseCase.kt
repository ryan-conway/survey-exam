package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.repository.SurveyRepository

class GetSurveysUseCase(private val repository: SurveyRepository) {

    suspend fun execute(token: Token): Result<List<Survey>> {
        return try {
            val surveys = repository.getSurveys(token)
            Result.Success(surveys)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}