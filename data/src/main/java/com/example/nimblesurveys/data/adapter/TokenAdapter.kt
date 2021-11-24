package com.example.nimblesurveys.data.adapter

import com.example.nimblesurveys.data.api.auth.AccessTokenAttributes
import com.example.nimblesurveys.data.api.auth.SignInAttributes
import com.example.nimblesurveys.data.cache.TokenEntity
import com.example.nimblesurveys.domain.model.Token

class TokenAdapter {
    fun toEntity(apiToken: AccessTokenAttributes) = TokenEntity(
        refreshToken = apiToken.refreshToken,
        accessToken = apiToken.accessToken,
        tokenType = apiToken.tokenType,
        expiry = apiToken.createdAt + apiToken.expiresIn,
    )

    fun toEntity(apiToken: SignInAttributes) = TokenEntity(
        refreshToken = apiToken.refreshToken,
        accessToken = apiToken.accessToken,
        tokenType = apiToken.tokenType,
        expiry = (apiToken.createdAt + apiToken.expiresIn) * 1000,
    )

    fun toToken(entity: TokenEntity) = Token(
        tokenType = entity.tokenType,
        refreshToken = entity.refreshToken,
        accessToken = entity.accessToken,
        expiry = entity.expiry,
    )
}