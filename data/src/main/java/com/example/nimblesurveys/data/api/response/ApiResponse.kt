package com.example.nimblesurveys.data.api.response

import com.squareup.moshi.Json

data class ApiResponse<T>(
    @Json(name = "data") val data: Data<T>
)

data class Data<T>(
    @Json(name = "id") val id: Long,
    @Json(name = "type") val type: String,
    @Json(name = "attributes") val attributes: T
)
