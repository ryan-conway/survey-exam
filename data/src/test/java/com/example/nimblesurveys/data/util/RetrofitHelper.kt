package com.example.nimblesurveys.data.util

import com.example.nimblesurveys.data.api.SurveyApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


fun getRetrofitFake(interceptor: Interceptor): Retrofit {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    return Retrofit.Builder()
        .baseUrl(SurveyApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}