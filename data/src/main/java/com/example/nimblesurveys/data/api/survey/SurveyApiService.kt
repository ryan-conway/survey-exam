package com.example.nimblesurveys.data.api.survey

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SurveyApiService {

    @GET("api/v1/surveys")
    suspend fun getSurveys(
        @Header("Authorization") authorization: String,
        @Query("page[number]") page: Int = 1,
        @Query("page[size]") pageSize: Int
    ): SurveyListResponse
}