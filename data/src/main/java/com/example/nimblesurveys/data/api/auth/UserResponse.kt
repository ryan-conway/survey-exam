package com.example.nimblesurveys.data.api.auth

import com.squareup.moshi.Json

data class UserAttributes(
    @Json(name = "email") val email: String,
    @Json(name = "avatar_url") val avatarUrl: String,
)