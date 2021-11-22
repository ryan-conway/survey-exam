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
        val uriBase = if (uri.contains('?')) {
            uri.substring(0, uri.indexOf('?'))
        } else {
            uri
        }
        val responseString = when {
            uriBase.endsWith("api/v1/oauth/token") -> LOGIN_RESPONSE_SUCCESS
            uriBase.endsWith("api/v1/me") -> USER_RESPONSE_SUCCESS
            uriBase.endsWith("api/v1/surveys") -> GET_SURVEY_LIST_RESPONSE_SUCCESS
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