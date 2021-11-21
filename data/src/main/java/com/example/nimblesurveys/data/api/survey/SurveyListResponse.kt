package com.example.nimblesurveys.data.api.survey

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyListResponse(
    @Json(name = "data") val data: List<Data>,
    @Json(name = "meta") val meta: Meta
)

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "id") val id: String,
    @Json(name = "type") val type: String,
    @Json(name = "attributes") val attributes: SurveyAttributes,
    @Json(name = "relationships") val relationships: Relationship,
)

@JsonClass(generateAdapter = true)
data class SurveyAttributes(
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "thank_email_above_threshold") val thankEmailAboveThreshold: String,
    @Json(name = "thank_email_below_threshold") val thankEmailBelowThreshold: String,
    @Json(name = "is_active") val isActive: String,
    @Json(name = "cover_image_url") val coverImageUrl: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "active_at") val activeAt: String,
    @Json(name = "inactive_at") val inactiveAt: String,
    @Json(name = "survey_type") val surveyType: String,
)

@JsonClass(generateAdapter = true)
data class Relationship(
    @Json(name = "questions") val questions: Questions
)

@JsonClass(generateAdapter = true)
data class Questions(
    @Json(name = "data") val data: RelationshipData
)

@JsonClass(generateAdapter = true)
data class RelationshipData(
    @Json(name = "id") val id: String,
    @Json(name = "type") val type: String
)

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "page") val page: Int,
    @Json(name = "pages") val pages: Int,
    @Json(name = "page_size") val pageSize: Int,
    @Json(name = "records") val records: Int,
)