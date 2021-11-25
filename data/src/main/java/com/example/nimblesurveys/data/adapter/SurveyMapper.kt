package com.example.nimblesurveys.data.adapter

import com.example.nimblesurveys.data.api.survey.Data
import com.example.nimblesurveys.data.cache.SurveyEntity
import com.example.nimblesurveys.domain.model.Survey

fun Data.toSurvey() = Survey(
    id = id,
    title = attributes.title,
    description = attributes.description,
    thankEmailAboveThreshold = attributes.thankEmailAboveThreshold,
    thankEmailBelowThreshold = attributes.thankEmailBelowThreshold,
    isActive = attributes.isActive,
    coverImageUrl = attributes.coverImageUrl,
    createdAt = attributes.createdAt,
    activeAt = attributes.activeAt,
    inactiveAt = attributes.inactiveAt,
    surveyType = attributes.surveyType,
)

fun SurveyEntity.toSurvey() = Survey(
    id = id,
    title = title,
    description = description,
    thankEmailAboveThreshold = thankEmailAboveThreshold,
    thankEmailBelowThreshold = thankEmailBelowThreshold,
    isActive = isActive,
    coverImageUrl = coverImageUrl,
    createdAt = createdAt,
    activeAt = activeAt,
    inactiveAt = inactiveAt,
    surveyType = surveyType,
)

fun Survey.toEntity() = SurveyEntity(
    id = id,
    title = title,
    description = description,
    thankEmailAboveThreshold = thankEmailAboveThreshold,
    thankEmailBelowThreshold = thankEmailBelowThreshold,
    isActive = isActive,
    coverImageUrl = coverImageUrl,
    createdAt = createdAt,
    activeAt = activeAt,
    inactiveAt = inactiveAt,
    surveyType = surveyType,
)