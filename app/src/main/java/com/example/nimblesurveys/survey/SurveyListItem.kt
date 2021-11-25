package com.example.nimblesurveys.survey

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SurveyListItem(
    val id: String,
    val name: String,
    val description: String,
    val coverImageUrl: String,
    val coverImageThumbnailUrl: String,
    val date: String
): Parcelable