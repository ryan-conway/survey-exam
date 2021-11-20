package com.example.nimblesurveys.data.api

import com.example.nimblesurveys.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody

class FakeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!BuildConfig.DEBUG) throw IllegalAccessError("For debugging only")

        val uri = chain.request().url().uri().toString()
        val responseString = when {
            uri.endsWith("api/v1/oauth/token") -> LOGIN_RESPONSE_SUCCESS
            uri.endsWith("api/v1/me") -> USER_RESPONSE_SUCCESS
            else -> ""
        }
        return chain.proceed(chain.request())
            .newBuilder()
            .code(200)
            .message(responseString)
            .body(
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    responseString.toByteArray()
                )
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}