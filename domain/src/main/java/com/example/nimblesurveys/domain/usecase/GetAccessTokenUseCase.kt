package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.repository.AuthRepository

class GetAccessTokenUseCase(private val repository: AuthRepository) {

    suspend fun execute(refreshToken: String): Result<Token> {
        return try {
            val token = repository.getAccessToken(refreshToken)
            Result.Success(token)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}