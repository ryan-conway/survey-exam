package com.example.nimblesurveys.data.api.auth

import retrofit2.http.Query

data class SignInRequest(
    @Query("grant_type") val grantType: String,
    @Query("email") val email: String,
    @Query("password") val password: String,
    @Query("client_id") val clientId: String,
    @Query("client_secret") val clientSecret: String
)





