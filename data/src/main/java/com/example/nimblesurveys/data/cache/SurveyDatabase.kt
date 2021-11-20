package com.example.nimblesurveys.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TokenEntity::class],
    exportSchema = false,
    version = 1
)
abstract class SurveyDatabase: RoomDatabase() {

    abstract fun authDao(): AuthDao
}