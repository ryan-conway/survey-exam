package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.adapter.SurveyAdapter
import com.example.nimblesurveys.data.api.survey.SurveyApiService
import com.example.nimblesurveys.data.cache.SurveyDao
import com.example.nimblesurveys.domain.model.Answer
import com.example.nimblesurveys.domain.model.Question
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.repository.SurveyRepository

class SurveyRepositoryImpl(
    private val api: SurveyApiService,
    private val surveyDao: SurveyDao,
    private val adapter: SurveyAdapter,
) : SurveyRepository {

    override suspend fun getSurveys(token: Token): List<Survey> {
        var surveys = getCachedSurveys()
        if (surveys.isEmpty()) {
            surveys = getNewSurveys(token)
            surveyDao.insertSurveys(surveys.map { adapter.toEntity(it) })
        }
        return surveys
    }

    private fun getCachedSurveys(): List<Survey> {
        return surveyDao.getSurveys().map { adapter.toSurvey(it) }
    }

    private suspend fun getNewSurveys(token: Token): List<Survey> {
        val surveys = mutableListOf<Survey>()
        val authorization = "${token.tokenType} ${token.accessToken}"
        var currentPage = 1
        var pageCount = 1

        while (currentPage <= pageCount) {
            val response = api.getSurveys(
                authorization = authorization,
                pageSize = PAGE_SIZE
            )
            pageCount = response.meta.pages
            currentPage++

            val newSurveys = response.data.map { adapter.toSurvey(it.id, it.attributes) }
            surveys.addAll(newSurveys)
        }

        return surveys
    }

    override suspend fun getQuestions(surveyId: String): List<Question> {
        TODO("Not yet implemented")
    }

    override suspend fun getAnswers(questionId: String): List<Answer> {
        TODO("Not yet implemented")
    }
}

const val PAGE_SIZE = 10