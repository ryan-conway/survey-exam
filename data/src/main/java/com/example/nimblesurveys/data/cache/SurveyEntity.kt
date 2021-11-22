package com.example.nimblesurveys.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey")
data class SurveyEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "thank_email_above_threshold") val thankEmailAboveThreshold: String,
    @ColumnInfo(name = "thank_email_below_threshold") val thankEmailBelowThreshold: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "cover_image_url") val coverImageUrl: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "active_at") val activeAt: String,
    @ColumnInfo(name = "inactive_at") val inactiveAt: String?,
    @ColumnInfo(name = "survey_type") val surveyType: String,
)