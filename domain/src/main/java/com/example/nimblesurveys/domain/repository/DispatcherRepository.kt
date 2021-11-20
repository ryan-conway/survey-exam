package com.example.nimblesurveys.domain.repository

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherRepository {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}