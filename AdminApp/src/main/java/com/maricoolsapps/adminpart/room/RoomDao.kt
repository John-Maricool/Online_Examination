package com.maricoolsapps.adminpart.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

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