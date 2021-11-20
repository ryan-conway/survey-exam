package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.BuildConfig
import com.example.nimblesurveys.data.api.ApiCredential
import com.example.nimblesurveys.data.api.SurveyApi
import com.example.nimblesurveys.data.api.auth.AuthApiService
import com.example.nimblesurveys.domain.model.Token
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl

    private lateinit var apiService: AuthApiService
    private val credential = ApiCredential(API_KEY, API_SECRET)

    private val mockInterceptor = mock(MockInterceptor::class.java, CALLS_REAL_METHODS)

    @Before
    fun setUp() {
        apiService = getRetrofitMock(mockInterceptor).create(AuthApiService::class.java)
        repository = AuthRepositoryImpl(apiService, credential)
    }

    private fun getRetrofitMock(interceptor: Interceptor): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(SurveyApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Test
    fun login_success_returnToken() = runBlocking {
        loginSuccess()
        val result = repository.login(EMAIL, PASSWORD)
        assertThat(result, instanceOf(Token::class.java))
    }

    @Test
    fun login_success_tokenHasCorrectValues() = runBlocking {
        loginSuccess()
        val result = repository.login(EMAIL, PASSWORD)
        assertThat(result.accessToken, equalTo(ACCESS_TOKEN))
        assertThat(result.refreshToken, equalTo(REFRESH_TOKEN))
        assertThat(result.expiry, equalTo(CREATED_AT + EXPIRY))
    }

    @Test
    fun login_failure_exceptionThrown() = runBlocking {
        loginFailed()
        try {
            repository.login(EMAIL, PASSWORD)
            assert(false)
        } catch (e: HttpException) {
            assertThat(e.code(), `is`(401))
            assertThat(e.message(), `is`(LOGIN_RESPONSE_FAILED))
        }
    }

    @Test
    fun getAccessToken_success_returnToken() = runBlocking {
        getAccessTokenSuccess()
        val result = repository.getAccessToken(REFRESH_TOKEN)
        assertThat(result, instanceOf(Token::class.java))
    }

    @Test
    fun getAccessToken_success_tokenHasCorrectValues() = runBlocking {
        getAccessTokenSuccess()
        val result = repository.getAccessToken(REFRESH_TOKEN)
        assertThat(result.accessToken, equalTo(ACCESS_TOKEN))
        assertThat(result.refreshToken, equalTo(REFRESH_TOKEN))
        assertThat(result.expiry, equalTo(CREATED_AT + EXPIRY))
    }

    @Test
    fun getAccessToken_failure_exceptionThrown() = runBlocking {
        getAccessTokenFailure()
        try {
            repository.getAccessToken(REFRESH_TOKEN)
            assert(false)
        } catch (e: HttpException) {
            assertThat(e.code(), `is`(401))
            assertThat(e.message(), `is`(LOGIN_RESPONSE_FAILED))
        }
    }

    private fun loginSuccess() {
        `when`(mockInterceptor.getResponse()).thenReturn(LOGIN_RESPONSE_SUCCESS)
        `when`(mockInterceptor.getCode()).thenReturn(200)
    }

    private fun loginFailed() {
        `when`(mockInterceptor.getResponse()).thenReturn(LOGIN_RESPONSE_FAILED)
        `when`(mockInterceptor.getCode()).thenReturn(401)
    }

    private fun getAccessTokenSuccess() {
        `when`(mockInterceptor.getResponse()).thenReturn(LOGIN_RESPONSE_SUCCESS)
        `when`(mockInterceptor.getCode()).thenReturn(200)
    }

    private fun getAccessTokenFailure() {
        `when`(mockInterceptor.getResponse()).thenReturn(LOGIN_RESPONSE_FAILED)
        `when`(mockInterceptor.getCode()).thenReturn(401)
    }
}

abstract class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!BuildConfig.DEBUG) throw IllegalAccessError("For debugging only")

        val responseString = getResponse()
        return chain.proceed(chain.request())
            .newBuilder()
            .code(getCode())
            .message(responseString)
            .body(
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    responseString.toByteArray()
                )
            )
            .addHeader("content-type", "application/json")
            .build()
    }

    abstract fun getResponse(): String
    abstract fun getCode(): Int
}

const val ID = 1
const val EMAIL = "test@abc.com"
const val PASSWORD = "password"
const val API_KEY = "apikey"
const val API_SECRET = "apisecret"
const val ACCESS_TOKEN = "accesstoken"
const val REFRESH_TOKEN = "refreshtoken"
const val EXPIRY = 7200L
const val CREATED_AT = 1597169495L

const val LOGIN_RESPONSE_SUCCESS = """
{
  "data": {
    "id": $ID,
    "type": "token",
    "attributes": {
      "access_token": "$ACCESS_TOKEN",
      "token_type": "Bearer",
      "expires_in": $EXPIRY,
      "refresh_token": "$REFRESH_TOKEN",
      "created_at": $CREATED_AT
    }
  }
}
"""

const val LOGIN_RESPONSE_FAILED = """
{
  "errors": [
    {
      "source": "Doorkeeper::OAuth::Error",
      "detail": "Client authentication failed due to unknown client, no client authentication included, or unsupported authentication method.",
      "code": "invalid_client"
    }
  ]
}
"""