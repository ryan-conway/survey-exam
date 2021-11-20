package com.example.nimblesurveys.data.api.auth

import com.example.nimblesurveys.data.api.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface AuthApiService {

    @GET("api/v1/oauth/token")
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ): ApiResponse<SignInResponse>
}