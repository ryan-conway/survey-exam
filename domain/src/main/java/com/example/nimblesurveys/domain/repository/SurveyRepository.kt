package com.example.nimblesurveys.domain.repository

import com.example.nimblesurveys.domain.model.Answer
import com.example.nimblesurveys.domain.model.Question
import com.example.nimblesurveys.domain.model.Survey

interface SurveyRepository {
    suspend fun getSurveys(): List<Survey>
    suspend fun getQuestions(surveyId: String): List<Question>
    suspend fun getAnswers(questionId: String): List<Answer>
}