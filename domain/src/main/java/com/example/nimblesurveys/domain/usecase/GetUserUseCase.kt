package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.User
import com.example.nimblesurveys.domain.repository.AuthRepository

class GetUserUseCase(private val repository: AuthRepository) {

    suspend fun execute(accessToken: String): Result<User> {
        return try {
            val user = repository.getUser(accessToken)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}