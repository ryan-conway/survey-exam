package com.example.nimblesurveys.data.api.auth

import retrofit2.http.Query

data class AccessTokenRequest(
    @Query("grant_type") val grantType: String = "password",
    @Query("refresh_token") val refreshToken: String,
    @Query("client_id") val clientId: String,
    @Query("client_secret") val clientSecret: String
)
