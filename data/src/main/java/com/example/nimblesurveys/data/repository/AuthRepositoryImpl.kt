package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.data.api.ApiCredential
import com.example.nimblesurveys.data.api.auth.AuthApiService
import com.example.nimblesurveys.data.api.auth.SignInRequest
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.model.User
import com.example.nimblesurveys.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApiService,
    private val apiCredential: ApiCredential
): AuthRepository {

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
            accessToken = signInResult.accessToken,
            refreshToken = signInResult.refreshToken,
            expiry = signInResult.createdAt + signInResult.expiresIn
        )
    }

    override suspend fun getAccessToken(refreshToken: String): Token {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(accessToken: String): User {
        TODO("Not yet implemented")
    }
}