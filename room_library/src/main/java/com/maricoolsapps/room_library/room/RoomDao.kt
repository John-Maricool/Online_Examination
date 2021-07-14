package com.maricoolsapps.room_library.room

import androidx.room.*

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuiz(quiz: RoomEntity)

    @Insert
    fun insertQuiz(quiz: List<RoomEntity>)

    @Update
    fun updateQuiz(quiz: RoomEntity)

    @Delete
    suspend fun deleteQuiz(quiz: List<RoomEntity>)

    @Query("select * from question_table order by id")
    fun getAllQuiz(): List<RoomEntity>

    }