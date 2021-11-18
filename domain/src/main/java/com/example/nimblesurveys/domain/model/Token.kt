package com.example.nimblesurveys.domain.model

data class Token(
    val accessToken: String?,
    val refreshToken: String,
    val expiry: Long
)
