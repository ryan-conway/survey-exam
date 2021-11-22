package com.example.nimblesurveys.domain.repository

import com.example.nimblesurveys.domain.model.Answer
import com.example.nimblesurveys.domain.model.Question
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.model.Token

interface SurveyRepository {
    suspend fun getSurveys(token: Token): List<Survey>
    suspend fun getSurvey(surveyId: String): Survey?
    suspend fun getQuestions(surveyId: String): List<Question>
    suspend fun getAnswers(questionId: String): List<Answer>
}