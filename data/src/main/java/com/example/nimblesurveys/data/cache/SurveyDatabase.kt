package com.example.nimblesurveys.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SurveyEntity::class,
        TokenEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class SurveyDatabase : RoomDatabase() {

    abstract fun authDao(): AuthDao
    abstract fun surveyDao(): SurveyDao
}