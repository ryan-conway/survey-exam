package com.example.nimblesurveys.util

import com.example.nimblesurveys.domain.repository.DispatcherRepository
import kotlinx.coroutines.Dispatchers

class DispatcherRepositoryImpl: DispatcherRepository {
    override fun io() = Dispatchers.IO
    override fun main() = Dispatchers.Main
}