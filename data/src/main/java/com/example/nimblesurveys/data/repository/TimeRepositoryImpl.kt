package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.domain.repository.TimeRepository

class TimeRepositoryImpl: TimeRepository {
    override fun getCurrentTime() = System.currentTimeMillis()
}