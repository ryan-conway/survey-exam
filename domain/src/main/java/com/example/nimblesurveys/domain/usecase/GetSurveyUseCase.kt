package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.exception.MissingSurveyException
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.repository.SurveyRepository

class GetSurveyUseCase(private val repository: SurveyRepository) {

    suspend fun execute(surveyId: String): Result<Survey> {
        return try {
            val survey = repository.getSurvey(surveyId) ?: throw MissingSurveyException()
            Result.Success(survey)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}