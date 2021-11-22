package com.example.nimblesurveys.data.adapter

import com.example.nimblesurveys.data.api.survey.SurveyAttributes
import com.example.nimblesurveys.data.cache.SurveyEntity
import com.example.nimblesurveys.domain.model.Survey

class SurveyAdapter {
    fun toSurvey(id: String, apiSurvey: SurveyAttributes) = Survey(
        id = id,
        title = apiSurvey.title,
        description = apiSurvey.description,
        thankEmailAboveThreshold = apiSurvey.thankEmailAboveThreshold,
        thankEmailBelowThreshold = apiSurvey.thankEmailBelowThreshold,
        isActive = apiSurvey.isActive,
        coverImageUrl = apiSurvey.coverImageUrl,
        createdAt = apiSurvey.createdAt,
        activeAt = apiSurvey.activeAt,
        inactiveAt = apiSurvey.inactiveAt,
        surveyType = apiSurvey.surveyType,
    )

    fun toSurvey(entity: SurveyEntity) = Survey(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        thankEmailAboveThreshold = entity.thankEmailAboveThreshold,
        thankEmailBelowThreshold = entity.thankEmailBelowThreshold,
        isActive = entity.isActive,
        coverImageUrl = entity.coverImageUrl,
        createdAt = entity.createdAt,
        activeAt = entity.activeAt,
        inactiveAt = entity.inactiveAt,
        surveyType = entity.surveyType,
    )

    fun toEntity(survey: Survey) = SurveyEntity(
        id = survey.id,
        title = survey.title,
        description = survey.description,
        thankEmailAboveThreshold = survey.thankEmailAboveThreshold,
        thankEmailBelowThreshold = survey.thankEmailBelowThreshold,
        isActive = survey.isActive,
        coverImageUrl = survey.coverImageUrl,
        createdAt = survey.createdAt,
        activeAt = survey.activeAt,
        inactiveAt = survey.inactiveAt,
        surveyType = survey.surveyType,
    )
}