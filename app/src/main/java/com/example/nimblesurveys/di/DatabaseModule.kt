package com.example.nimblesurveys.di

import android.content.Context
import androidx.room.Room
import com.example.nimblesurveys.data.cache.SurveyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SurveyDatabase {
        return Room.databaseBuilder(
            context,
            SurveyDatabase::class.java,
            "survey.db"
        ).build()
    }
}