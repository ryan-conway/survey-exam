package com.example.nimblesurveys.data.api.auth

import com.example.nimblesurveys.domain.exception.UnauthorizedException
import com.example.nimblesurveys.domain.model.Token
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {

    private var token: Token? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val hasAuth = chain.request().header("No-Authentication") == null
        val requestBuilder = chain.request().newBuilder()
        if (hasAuth) {
            val token = token
            if (token == null) {
                throw UnauthorizedException()
            } else {
                val authorization = "${token.tokenType} ${token.accessToken}"
                requestBuilder.addHeader("Authorization", authorization)
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    fun setAccessToken(token: Token) {
        this.token = token
    }
}