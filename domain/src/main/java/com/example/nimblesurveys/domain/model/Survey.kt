package com.example.nimblesurveys.domain.model

data class Survey(
    val id: String,
    val title: String,
    val description: String,
    val thankEmailAboveThreshold: String?,
    val thankEmailBelowThreshold: String?,
    val isActive: Boolean,
    val coverImageUrl: String,
    val createdAt: String,
    val activeAt: String,
    val inactiveAt: String?,
    val surveyType: String,
)
