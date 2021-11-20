package com.example.nimblesurveys.data.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    @Json(name = "data") val data: Data<T>
)

@JsonClass(generateAdapter = true)
data class Data<T>(
    @Json(name = "id") val id: Long,
    @Json(name = "type") val type: String,
    @Json(name = "attributes") val attributes: T
)
