package com.example.nimblesurveys.data.api.auth

import com.squareup.moshi.Json

data class SignInRequest(
    @Json(name = "grant_type") val grantType: String = "password",
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "client_id") val clientId: String,
    @Json(name = "client_secret") val clientSecret: String
)