package com.example.nimblesurveys.domain.model

data class Question(
    val text: String,
    val help_text: String?,
    val display_order: Int,
    val short_text: String,
    val pick: String,
    val display_type: String,
    val is_mandatory: Boolean,
    val correct_answer_id: String?,
    val facebook_profile: String?,
    val twitter_profile: String?,
    val image_url: String,
    val cover_image_url: String,
    val cover_image_opacity: Float,
    val cover_background_color: String?,
    val is_shareable_on_facebook: Boolean,
    val is_shareable_on_twitter: Boolean,
    val font_face: String?,
    val font_size: Int?,
    val tag_list: String,
)