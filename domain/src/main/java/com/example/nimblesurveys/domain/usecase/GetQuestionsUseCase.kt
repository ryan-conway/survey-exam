package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.model.Question
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.repository.SurveyRepository

class GetQuestionsUseCase(private val repository: SurveyRepository) {

    suspend fun execute(surveyId: String): Result<List<Question>> {
        return try {
            val questions = repository.getQuestions(surveyId)
            Result.Success(questions)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}