package com.example.nimblesurveys.data.api.auth

import com.squareup.moshi.Json

data class AccessTokenRequest(
    @Json(name = "grant_type") val grantType: String = "password",
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "client_id") val clientId: String,
    @Json(name = "client_secret") val clientSecret: String
)
