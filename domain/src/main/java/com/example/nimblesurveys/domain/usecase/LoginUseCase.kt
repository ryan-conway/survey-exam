package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.exception.MissingCredentialsException
import com.example.nimblesurveys.domain.model.Token
import com.example.nimblesurveys.domain.repository.AuthRepository
import com.example.nimblesurveys.domain.model.Result

class LoginUseCase(private val repository: AuthRepository) {

    suspend fun execute(username: String, password: String): Result<Token> {
        if (username.isEmpty() || password.isEmpty()) {
            return Result.Failure(MissingCredentialsException())
        }

        return try {
            val token = repository.login(username, password)
            Result.Success(token)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}