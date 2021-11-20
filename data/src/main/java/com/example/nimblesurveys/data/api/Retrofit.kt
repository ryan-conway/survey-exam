package com.example.nimblesurveys.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SurveyApi {

    fun getOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()

    fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    inline fun <reified T> getService(): T {
        val okHttpClient = getOkHttpClient()
        val retrofit = getRetrofit(okHttpClient)
        return retrofit.create(T::class.java)
    }
}