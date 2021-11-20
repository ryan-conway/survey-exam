package com.example.nimblesurveys.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToken(token: TokenEntity)

    @Query("SELECT * FROM token LIMIT 1")
    fun getToken(): TokenEntity?

    @Query("DELETE FROM token")
    fun deleteToken()
}