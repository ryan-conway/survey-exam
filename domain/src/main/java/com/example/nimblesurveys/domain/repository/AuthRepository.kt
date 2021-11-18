package com.example.nimblesurveys.domain.repository

import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Token
    suspend fun getAccessToken(refreshToken: String): Token
    suspend fun getUser(accessToken: String): User
}