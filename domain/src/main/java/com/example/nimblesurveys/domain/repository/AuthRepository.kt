package com.example.nimblesurveys.domain.repository

import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Token
    suspend fun getAccessToken(): Token?
    suspend fun refreshAccessToken()
    suspend fun getUser(): User
    suspend fun isLoggedIn(): Boolean
}