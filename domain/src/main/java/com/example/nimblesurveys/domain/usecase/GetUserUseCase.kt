package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.exception.MissingRefreshTokenException
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.User
import com.example.nimblesurveys.domain.repository.AuthRepository

class GetUserUseCase(private val repository: AuthRepository) {

    suspend fun execute(): Result<User> {
        return try {
            val token = repository.getAccessToken() ?: throw MissingRefreshTokenException()
            val user = repository.getUser(token)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}