package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.exception.MissingCredentialsException
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer

@RunWith(MockitoJUnitRunner::class)
class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase

    private val mockRepository = mock(AuthRepository::class.java)
    private val token = Token(
        tokenType = TOKEN_TYPE,
        accessToken = ACCESS_TOKEN,
        refreshToken = REFRESH_TOKEN,
        expiry = CREATED_AT + EXPIRY
    )

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(mockRepository)
    }

    @Test
    fun login_success_returnSuccess() = runBlocking {
        success()
        val result = loginUseCase.execute(EMAIL, PASSWORD)
        assertThat(result, instanceOf(Result.Success::class.java))
    }

    @Test
    fun login_success_returnToken() = runBlocking {
        success()
        val result = loginUseCase.execute(EMAIL, PASSWORD)
        val token = (result as Result.Success).value
        assertThat(token, instanceOf(Token::class.java))
    }

    @Test
    fun login_failure_returnFailure() = runBlocking {
        missingCredentials()
        val result = loginUseCase.execute("", "")
        assertThat(result, instanceOf(Result.Failure::class.java))
    }

    @Test
    fun login_missingUsername_returnMissingCredentials() = runBlocking {
        missingCredentials()
        val result = loginUseCase.execute("", PASSWORD)
        val exception = (result as Result.Failure).cause
        assertThat(exception, instanceOf(MissingCredentialsException::class.java))
    }

    @Test
    fun login_missingPassword_returnMissingCredentials() = runBlocking {
        missingCredentials()
        val result = loginUseCase.execute(EMAIL, "")
        val exception = (result as Result.Failure).cause
        assertThat(exception, instanceOf(MissingCredentialsException::class.java))
    }

    @Test
    fun login_missingUsernameAndPassword_returnMissingCredentials() = runBlocking {
        missingCredentials()
        val result = loginUseCase.execute("", "")
        val exception = (result as Result.Failure).cause
        assertThat(exception, instanceOf(MissingCredentialsException::class.java))
    }

    private suspend fun success() {
        `when`(mockRepository.login(EMAIL, PASSWORD)).thenReturn(token)
    }

    private suspend fun missingCredentials() {
        lenient().`when`(mockRepository.login(EMAIL, PASSWORD))
            .doAnswer { throw MissingCredentialsException() }
    }
}

const val EMAIL = "test@abc.com"
const val PASSWORD = "password"
const val ACCESS_TOKEN = "accesstoken"
const val REFRESH_TOKEN = "refreshtoken"
const val TOKEN_TYPE = "Bearer"
const val EXPIRY = 7200L
const val CREATED_AT = 1597169495L