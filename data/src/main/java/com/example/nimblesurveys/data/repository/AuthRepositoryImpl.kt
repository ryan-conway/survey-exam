package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.api.ApiCredential
import com.example.nimblesurveys.data.api.auth.AccessTokenRequest
import com.example.nimblesurveys.data.api.auth.AuthApiService
import com.example.nimblesurveys.data.api.auth.SignInRequest
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.model.User
import com.example.nimblesurveys.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApiService,
    private val apiCredential: ApiCredential
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
        return Token(
            tokenType = signInResult.tokenType,
            accessToken = signInResult.accessToken,
            refreshToken = signInResult.refreshToken,
            expiry = signInResult.createdAt + signInResult.expiresIn
        )
    }

    override suspend fun getAccessToken(refreshToken: String): Token {
        val accessTokenRequest = AccessTokenRequest(
            refreshToken = refreshToken,
            clientId = apiCredential.key,
            clientSecret = apiCredential.secret
        )
        val apiResponse = api.getAccessToken(accessTokenRequest)
        val getTokenResult = apiResponse.data.attributes
        return Token(
            tokenType = getTokenResult.tokenType,
            accessToken = getTokenResult.accessToken,
            refreshToken = getTokenResult.refreshToken,
            expiry = getTokenResult.createdAt + getTokenResult.expiresIn
        )
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
}