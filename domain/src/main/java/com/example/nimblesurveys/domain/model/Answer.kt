package com.example.nimblesurveys.domain.model

data class Answer(
    val text: String,
    val help_text: String?,
    val input_mask_placeholder: String?,
    val short_text: String,
    val is_mandatory: Boolean,
    val is_customer_first_name: Boolean,
    val is_customer_last_name: Boolean,
    val is_customer_title: Boolean,
    val is_customer_email: Boolean,
    val prompt_custom_answer: Boolean,
    val weight: Float,
    val display_order: Int,
    val display_type: String,
    val input_mask: String?,
    val date_constraint: String?,
    val default_value: String?,
    val response_class: String,
    val reference_identifier: String?,
    val score: Int?,
)
