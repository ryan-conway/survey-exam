package com.example.nimblesurveys.data.adapter

import com.example.nimblesurveys.data.api.auth.AccessTokenAttributes
import com.example.nimblesurveys.data.api.auth.SignInAttributes
import com.example.nimblesurveys.data.cache.TokenEntity
import com.example.nimblesurveys.domain.model.Token

fun AccessTokenAttributes.toEntity() = TokenEntity(
    refreshToken = refreshToken,
    accessToken = accessToken,
    tokenType = tokenType,
    expiry = createdAt + expiresIn,
)

fun SignInAttributes.toEntity() = TokenEntity(
    refreshToken = refreshToken,
    accessToken = accessToken,
    tokenType = tokenType,
    expiry = (createdAt + expiresIn) * 1000,
)

fun TokenEntity.toToken() = Token(
    tokenType = tokenType,
    refreshToken = refreshToken,
    accessToken = accessToken,
    expiry = expiry,
)