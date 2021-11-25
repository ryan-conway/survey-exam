package com.example.nimblesurveys.data.api.auth

import com.example.nimblesurveys.data.api.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/oauth/token")
    @Headers("No-Authentication: true")
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ): ApiResponse<SignInAttributes>

    @POST("api/v1/oauth/token")
    @Headers("No-Authentication: true")
    suspend fun getAccessToken(
        @Body accessTokenRequest: AccessTokenRequest
    ): ApiResponse<AccessTokenAttributes>

    @GET("api/v1/me")
    suspend fun getUser(
        @Header("Authorization") authorization: String
    ): ApiResponse<UserAttributes>
}