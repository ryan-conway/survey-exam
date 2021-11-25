package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.exception.UnauthorizedException
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.User
import com.example.nimblesurveys.domain.repository.AuthRepository

class GetUserUseCase(private val repository: AuthRepository) {

    suspend fun execute(): Result<User> {
        return try {
            getUserResult()
        } catch (e: UnauthorizedException) {
            repository.refreshAccessToken()
            getUserResult()
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }

    private suspend fun getUserResult(): Result<User> {
        val surveys = repository.getUser()
        return Result.Success(surveys)
    }
}