package com.example.nimblesurveys.data.adapter

import com.example.nimblesurveys.data.api.survey.SurveyAttributes
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
}