package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.adapter.toEntity
import com.example.nimblesurveys.data.adapter.toToken
import com.example.nimblesurveys.data.api.ApiCredential
import com.example.nimblesurveys.data.api.auth.AccessTokenAttributes
import com.example.nimblesurveys.data.api.auth.AccessTokenRequest
import com.example.nimblesurveys.data.api.auth.AuthApiService
import com.example.nimblesurveys.data.api.auth.SignInRequest
import com.example.nimblesurveys.data.cache.AuthDao
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.model.User
import com.example.nimblesurveys.domain.provider.TimeProvider
import com.example.nimblesurveys.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authDao: AuthDao,
    private val api: AuthApiService,
    private val apiCredential: ApiCredential,
    private val timeProvider: TimeProvider,
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
        authDao.insertToken(signInResult.toEntity())
        return Token(
            tokenType = signInResult.tokenType,
            accessToken = signInResult.accessToken,
            refreshToken = signInResult.refreshToken,
            expiry = signInResult.createdAt + signInResult.expiresIn
        )
    }

    override suspend fun getAccessToken(): Token? {
        val cachedToken = authDao.getToken() ?: return null
        val tokenEntity =
            if (cachedToken.expiry <= timeProvider.getCurrentTime()) {
                val newToken = fetchNewToken(cachedToken.refreshToken)
                val tokenEntity = newToken.toEntity()
                authDao.deleteToken()
                authDao.insertToken(tokenEntity)
                tokenEntity
            } else {
                cachedToken
            }
        return tokenEntity.toToken()
    }

    private suspend fun fetchNewToken(refreshToken: String): AccessTokenAttributes {
        val accessTokenRequest = AccessTokenRequest(
            refreshToken = refreshToken,
            clientId = apiCredential.key,
            clientSecret = apiCredential.secret
        )
        val apiResponse = api.getAccessToken(accessTokenRequest)
        return apiResponse.data.attributes
    }

    override suspend fun getUser(token: Token): User {
        val authorization = "${token.tokenType} ${token.accessToken}"
        val apiResponse = api.getUser(authorization)
        val getUserResult = apiResponse.data.attributes
        return User(
            id = apiResponse.data.id,
            email = getUserResult.email,
            avatar = getUserResult.avatarUrl
        )
    }

    override suspend fun isLoggedIn(): Boolean {
        return authDao.getToken() != null
    }
}