package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.adapter.SurveyAdapter
import com.example.nimblesurveys.data.api.survey.Meta
import com.example.nimblesurveys.data.api.survey.SurveyApiService
import com.example.nimblesurveys.data.api.survey.SurveyListResponse
import com.example.nimblesurveys.data.cache.SurveyDao
import com.example.nimblesurveys.data.cache.SurveyEntity
import com.example.nimblesurveys.data.util.MockInterceptor
import com.example.nimblesurveys.data.util.getRetrofitFake
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.model.Token
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException

@RunWith(MockitoJUnitRunner::class)
class SurveyRepositoryImplTest {

    private lateinit var repository: SurveyRepositoryImpl
    private lateinit var apiService: SurveyApiService
    private lateinit var surveyDaoFake: SurveyDaoFake

    private val mockApiService = mock(SurveyApiService::class.java)
    private val mockInterceptor = mock(MockInterceptor::class.java, CALLS_REAL_METHODS)

    private val token = Token(
        tokenType = TOKEN_TYPE,
        accessToken = ACCESS_TOKEN,
        refreshToken = REFRESH_TOKEN,
        expiry = CREATED_AT + EXPIRY
    )

    private val mockApiServiceResponse = SurveyListResponse(emptyArray(), Meta(0, 0, 0, 0))

    @Before
    fun setUp() {
        apiService = getRetrofitFake(mockInterceptor).create(SurveyApiService::class.java)
        surveyDaoFake = SurveyDaoFake()
        repository = SurveyRepositoryImpl(
            apiService,
            surveyDaoFake,
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
            assertThat(e.code(), `is`(401))
            assertThat(e.message(), `is`(RESPONSE_FAILED_EMPTY))
        }
    }

    @Test
    fun getSurveys_firstCall_fetchSurveysFromNetwork() = runBlocking {
        mockApiResponseSuccess()
        repository.getSurveys(token)
        verify(mockApiService).getSurveys(anyString(), anyInt(), anyInt())
        Unit
    }

    @Test
    fun getSurveys_firstCall_saveSurveysInCache() = runBlocking {
        mockApiResponseSuccess()
        assertThat(surveyDaoFake.surveysInserted, `is`(false))
        repository.getSurveys(token)
        assertThat(surveyDaoFake.surveysInserted, `is`(true))
    }

    @Test
    fun getSurveys_secondCall_returnSurveysFromCache() = runBlocking {
        mockApiResponseSuccess()
        repository.getSurveys(token)
        verify(mockApiService, times(1)).getSurveys(anyString(), anyInt(), anyInt())
        Unit
    }

    private fun getSurveysSuccess() {
        `when`(mockInterceptor.getResponse()).thenReturn(GET_SURVEY_LIST_RESPONSE_SUCCESS)
        `when`(mockInterceptor.getCode()).thenReturn(200)
    }

    private fun getSurveysFailure() {
        `when`(mockInterceptor.getResponse()).thenReturn(RESPONSE_FAILED_EMPTY)
        `when`(mockInterceptor.getCode()).thenReturn(401)
    }

    private suspend fun mockApiResponseSuccess() {
        `when`(mockApiService.getSurveys(anyString(), anyInt(), anyInt()))
            .thenReturn(mockApiServiceResponse)
        repository = SurveyRepositoryImpl(mockApiService, surveyDaoFake, SurveyAdapter())

    }

}

class SurveyDaoFake : SurveyDao {

    private var surveys = mutableListOf<SurveyEntity>()
    var surveysInserted: Boolean = false

    override fun insertSurveys(surveys: List<SurveyEntity>) {
        this.surveys.addAll(surveys)
        surveysInserted = true
    }

    override fun deleteSurveys() {
        surveys = mutableListOf()
    }

    override fun getSurveys(): List<SurveyEntity> {
        return surveys
    }

    override fun getSurvey(id: String): SurveyEntity? {
        return surveys.firstOrNull { it.id == id }
    }

}