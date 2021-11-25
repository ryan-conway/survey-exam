package com.example.nimblesurveys.data.repository

import com.example.nimblesurveys.domain.provider.TimeProvider

class TimeProviderImpl: TimeProvider {
    override fun getCurrentTime() = System.currentTimeMillis()
}