package com.example.nimblesurveys.domain.usecase

import com.example.nimblesurveys.domain.repository.AuthRepository

class IsLoggedInUseCase(private val repository: AuthRepository) {

    suspend fun execute() = repository.isLoggedIn()
}