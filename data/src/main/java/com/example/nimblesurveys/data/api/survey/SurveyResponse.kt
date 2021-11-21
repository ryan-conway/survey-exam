package com.example.nimblesurveys.data.api.survey

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyResponse(
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "thank_email_above_threshold") val thank_email_above_threshold: String,
    @Json(name = "thank_email_below_threshold") val thank_email_below_threshold: String,
    @Json(name = "is_active") val is_active: Boolean,
    @Json(name = "cover_image_url") val cover_image_url: String,
    @Json(name = "created_at") val created_at: String,
    @Json(name = "active_at") val active_at: String,
    @Json(name = "inactive_at") val inactive_at: String?,
    @Json(name = "survey_type") val survey_type: String,
)
