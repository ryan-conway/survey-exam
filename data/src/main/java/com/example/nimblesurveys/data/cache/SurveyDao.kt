package com.example.nimblesurveys.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSurveys(surveys: List<SurveyEntity>)

    @Query("DELETE FROM survey")
    fun deleteSurveys()

    @Query("""
        SELECT * FROM survey
        ORDER BY active_at ASC
    """)
    fun getSurveys(): List<SurveyEntity>

    @Query("""
        SELECT * FROM survey
        WHERE id = :id
        LIMIT 1
    """)
    fun getSurvey(id: String): SurveyEntity?
}