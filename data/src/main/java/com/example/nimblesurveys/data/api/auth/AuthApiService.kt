package com.example.nimblesurveys.data.api.auth

import com.example.nimblesurveys.data.api.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/oauth/token")
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ): ApiResponse<SignInAttributes>
}