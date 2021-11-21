package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.adapter.SurveyAdapter
import com.example.nimblesurveys.data.api.survey.SurveyApiService
import com.example.nimblesurveys.data.util.MockInterceptor
import com.example.nimblesurveys.data.util.getRetrofitFake
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.model.Token
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.CALLS_REAL_METHODS
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException

@RunWith(MockitoJUnitRunner::class)
class SurveyRepositoryImplTest {

    private lateinit var repository: SurveyRepositoryImpl
    private lateinit var apiService: SurveyApiService

    private val mockInterceptor = mock(MockInterceptor::class.java, CALLS_REAL_METHODS)

    private val token = Token(
        tokenType = TOKEN_TYPE,
        accessToken = ACCESS_TOKEN,
        refreshToken = REFRESH_TOKEN,
        expiry = CREATED_AT + EXPIRY
    )

    @Before
    fun setUp() {
        apiService = getRetrofitFake(mockInterceptor).create(SurveyApiService::class.java)
        repository = SurveyRepositoryImpl(
            apiService,
            SurveyAdapter()
        )
    }

    @Test
    fun getSurveys_success_returnSurveyList() = runBlocking {
        getSurveysSuccess()
        val result = repository.getSurveys(token)
        val survey = result.first()
        assertThat(survey, instanceOf(Survey::class.java))
    }

    @Test
    fun getSurveys_failure_exceptionThrown() = runBlocking {
        getSurveysFailure()
        try {
            repository.getSurveys(token)
            assert(false)
        } catch (e: HttpException) {
            assertThat(e.code(), CoreMatchers.`is`(401))
            assertThat(e.message(), CoreMatchers.`is`(RESPONSE_FAILED_EMPTY))
        }
    }

    private fun getSurveysSuccess() {
        Mockito.`when`(mockInterceptor.getResponse()).thenReturn(GET_SURVEY_LIST_RESPONSE_SUCCESS)
        Mockito.`when`(mockInterceptor.getCode()).thenReturn(200)
    }

    private fun getSurveysFailure() {
        Mockito.`when`(mockInterceptor.getResponse()).thenReturn(RESPONSE_FAILED_EMPTY)
        Mockito.`when`(mockInterceptor.getCode()).thenReturn(401)
    }

}