package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.adapter.TokenAdapter
import com.example.nimblesurveys.data.api.ApiCredential
import com.example.nimblesurveys.data.api.auth.AuthApiService
import com.example.nimblesurveys.data.cache.AuthDao
import com.example.nimblesurveys.data.cache.TokenEntity
import com.example.nimblesurveys.data.util.MockInterceptor
import com.example.nimblesurveys.data.util.getRetrofitFake
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.model.User
import com.example.nimblesurveys.domain.repository.TimeRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl

    private lateinit var authDao: AuthDaoFake
    private lateinit var apiService: AuthApiService
    private val credential = ApiCredential(API_KEY, API_SECRET)

    private val mockInterceptor = mock(MockInterceptor::class.java, CALLS_REAL_METHODS)
    private val timeRepository = mock(TimeRepository::class.java)

    private val token = Token(
        tokenType = TOKEN_TYPE,
        accessToken = ACCESS_TOKEN,
        refreshToken = REFRESH_TOKEN,
        expiry = CREATED_AT + EXPIRY
    )

    @Before
    fun setUp() {
        authDao = AuthDaoFake()
        apiService = getRetrofitFake(mockInterceptor).create(AuthApiService::class.java)
        repository = AuthRepositoryImpl(
            authDao,
            apiService,
            credential,
            timeRepository,
            TokenAdapter()
        )

        `when`(timeRepository.getCurrentTime()).thenReturn(CREATED_AT)
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
    fun getAccessToken_success_tokenIsCached() = runBlocking {
        getAccessTokenSuccess()
        assert(authDao.getToken() == null)
        repository.getAccessToken(REFRESH_TOKEN)
        assert(authDao.getToken() != null)
    }

    @Test
    fun getAccessToken_tokenExpired_cachedTokenIsDeleted() = runBlocking {
        getAccessTokenSuccess()
        repository.getAccessToken(REFRESH_TOKEN)
        val expiryTime = CREATED_AT + EXPIRY
        `when`(timeRepository.getCurrentTime()).thenReturn(expiryTime)
        repository.getAccessToken(REFRESH_TOKEN)
        assertThat(authDao.wasTokenDeleted, `is`(true))
    }

    @Test
    fun getAccessToken_tokenNotExpired_cachedTokenNotDeleted() = runBlocking {
        getAccessTokenSuccess()
        repository.getAccessToken(REFRESH_TOKEN)
        val currentTime = CREATED_AT + 1
        `when`(timeRepository.getCurrentTime()).thenReturn(currentTime)
        repository.getAccessToken(REFRESH_TOKEN)
        assertThat(authDao.wasTokenDeleted, `is`(false))
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

    @Test
    fun getUser_success_returnUser() = runBlocking {
        getUserSuccess()
        val result = repository.getUser(token)
        assertThat(result, instanceOf(User::class.java))
    }

    @Test
    fun getUser_success_userHasCorrectValues() = runBlocking {
        getUserSuccess()
        val result = repository.getUser(token)
        assertThat(result.email, `is`(EMAIL))
        assertThat(result.avatar, `is`(AVATAR_URL))
    }

    @Test
    fun getUser_success_correctValuesPassed() = runBlocking {
        getUserSuccess()
        repository.getUser(token)
        val authHeader = mockInterceptor.request.header("Authorization")
        assertThat(authHeader, `is`("${token.tokenType} ${token.accessToken}"))
    }

    @Test
    fun getUser_failure_exceptionThrown() = runBlocking {
        getUserFailure()
        try {
            repository.getUser(token)
            assert(false)
        } catch (e: HttpException) {
            assertThat(e.code(), `is`(401))
            assertThat(e.message(), `is`(RESPONSE_FAILED_EMPTY))
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

    private fun getUserSuccess() {
        `when`(mockInterceptor.getResponse()).thenReturn(USER_RESPONSE_SUCCESS)
        `when`(mockInterceptor.getCode()).thenReturn(200)
    }

    private fun getUserFailure() {
        `when`(mockInterceptor.getResponse()).thenReturn(RESPONSE_FAILED_EMPTY)
        `when`(mockInterceptor.getCode()).thenReturn(401)
    }
}

class AuthDaoFake : AuthDao {

    private var token: TokenEntity? = null
    var tokenInsertCount: Int = 0
    var wasTokenDeleted: Boolean = false

    override fun insertToken(token: TokenEntity) {
        this.token = token
        tokenInsertCount++
    }

    override fun getToken(): TokenEntity? = token

    override fun deleteToken() {
        if (token != null) {
            wasTokenDeleted = true
        }
        this.token = null
    }

}

const val ID = 1
const val EMAIL = "test@abc.com"
const val PASSWORD = "password"
const val API_KEY = "apikey"
const val API_SECRET = "apisecret"
const val ACCESS_TOKEN = "accesstoken"
const val REFRESH_TOKEN = "refreshtoken"
const val TOKEN_TYPE = "Bearer"
const val EXPIRY = 7200L
const val CREATED_AT = 1597169495L
const val AVATAR_URL = "avatarurl"

const val LOGIN_RESPONSE_SUCCESS = """
{
  "data": {
    "id": $ID,
    "type": "token",
    "attributes": {
      "access_token": "$ACCESS_TOKEN",
      "token_type": "$TOKEN_TYPE",
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

const val USER_RESPONSE_SUCCESS = """
{
  "data": {
    "id": $ID,
    "type": "user",
    "attributes": {
      "email": "$EMAIL",
      "avatar_url": "$AVATAR_URL"
    }
  }
}
"""

const val RESPONSE_FAILED_EMPTY = "{}"