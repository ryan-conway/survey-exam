package com.example.nimblesurveys.data.api.auth

import com.squareup.moshi.Json

data class SignInResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in") val expiresIn: Long,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "created_at") val createdAt: Long
)