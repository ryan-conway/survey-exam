package com.example.nimblesurveys.data.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    @Json(name = "data") val data: Data<T>,
    @Json(name = "meta") val meta: Meta?
)
