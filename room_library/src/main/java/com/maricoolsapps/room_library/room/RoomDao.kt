package com.maricoolsapps.room_library.room

import androidx.room.*

@Dao
interface RoomDao {

    @Insert
    fun insertQuiz(quiz: RoomEntity)

    @Update
    fun updateQuiz(quiz: RoomEntity)

    @Delete
    fun deleteQuiz(quiz: List<RoomEntity>)

    @Query("select * from question_table order by id")
    fun getAllQuiz(): List<RoomEntity>

    }