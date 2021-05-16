package com.maricoolsapps.adminpart.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomDao {

    @Insert
    fun insertQuiz(quiz: RoomEntity)

    @Update
    fun updateQuiz(quiz: RoomEntity)

    @Delete
    fun deleteQuiz(quiz: RoomEntity)

    @Query("select * from question_table order by id")
    fun getAllQuiz(): LiveData<List<RoomEntity>>
}