package com.example.nimblesurveys.survey

import com.example.nimblesurveys.domain.model.Survey

fun Survey.toListItem() = SurveyListItem(
    id = id,
    name = title,
    description = description,
    coverImageUrl = coverImageUrl,
    coverImageThumbnailUrl = "${coverImageUrl}l",
    date = activeAt,
)