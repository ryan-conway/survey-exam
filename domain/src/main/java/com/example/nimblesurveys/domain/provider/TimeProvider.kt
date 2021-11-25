package com.example.nimblesurveys.domain.provider

interface TimeProvider {
    fun getCurrentTime(): Long
}