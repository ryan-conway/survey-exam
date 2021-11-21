package com.example.nimblesurveys.data.util

import com.example.nimblesurveys.data.BuildConfig
import okhttp3.*

abstract class MockInterceptor : Interceptor {

    lateinit var request: Request

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!BuildConfig.DEBUG) throw IllegalAccessError("For debugging only")

        request = chain.request()
        val responseString = getResponse()
        return chain.proceed(chain.request())
            .newBuilder()
            .code(getCode())
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

    abstract fun getResponse(): String
    abstract fun getCode(): Int
}