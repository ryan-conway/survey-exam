package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.model.Answer
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.repository.SurveyRepository

class GetAnswersUseCase(private val repository: SurveyRepository) {

    suspend fun execute(questionId: String): Result<List<Answer>> {
        return try {
            val answers = repository.getAnswers(questionId)
            Result.Success(answers)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}