package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.adapter.toEntity
import com.example.nimblesurveys.data.adapter.toToken
import com.example.nimblesurveys.data.api.ApiCredential
import com.example.nimblesurveys.data.api.auth.AccessTokenRequest
import com.example.nimblesurveys.data.api.auth.AuthApiService
import com.example.nimblesurveys.data.api.auth.AuthInterceptor
import com.example.nimblesurveys.data.api.auth.SignInRequest
import com.example.nimblesurveys.data.cache.AuthDao
import com.example.nimblesurveys.domain.exception.UnauthorizedException
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.model.User
import com.example.nimblesurveys.domain.repository.AuthRepository
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val authDao: AuthDao,
    private val api: AuthApiService,
    private val apiCredential: ApiCredential,
    private val authInterceptor: AuthInterceptor
) : AuthRepository {

    @Throws(Throwable::class)
    override suspend fun login(username: String, password: String): Token {
        val signInRequest = SignInRequest(
            email = username,
            password = password,
            clientId = apiCredential.key,
            clientSecret = apiCredential.secret
        )
        val apiResponse = api.signIn(signInRequest)
        val signInResult = apiResponse.data.attributes
        authDao.deleteToken()
        val tokenEntity = signInResult.toEntity()
        authDao.insertToken(tokenEntity)
        val token = tokenEntity.toToken()
        setInterceptorAuthToken(token)
        return token
    }

    override suspend fun getAccessToken(): Token? {
        return authDao.getToken()?.toToken()
    }

    override suspend fun refreshAccessToken() {
        val cachedToken = getAccessToken() ?: throw UnauthorizedException()
        val accessTokenRequest = AccessTokenRequest(
            refreshToken = cachedToken.refreshToken,
            clientId = apiCredential.key,
            clientSecret = apiCredential.secret
        )
        val apiResponse = try {
            api.getAccessToken(accessTokenRequest)
        } catch (e: HttpException) {
            throw e
        }
        val tokenEntity = apiResponse.data.attributes.toEntity()
        authDao.deleteToken()
        authDao.insertToken(tokenEntity)
        val token = tokenEntity.toToken()
        setInterceptorAuthToken(token)
    }

    override suspend fun getUser(): User {
        val apiResponse = api.getUser()
        val getUserResult = apiResponse.data.attributes
        return User(
            id = apiResponse.data.id,
            email = getUserResult.email,
            avatar = getUserResult.avatarUrl
        )
    }

    override suspend fun isLoggedIn(): Boolean {
        val cachedToken = authDao.getToken()?.toToken()
        return if (cachedToken == null) {
            false
        } else {
            setInterceptorAuthToken(cachedToken)
            true
        }
    }

    private fun setInterceptorAuthToken(token: Token) {
        authInterceptor.setAccessToken(token)
    }
}