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
    suspend fun deleteQuiz(quiz: RoomEntity): Int

    @Query("select * from question_table order by id")
    fun getAllQuiz(): List<RoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity)

    @Delete
    suspend fun deleteResult(result: List<QuizResultEntity>)

    @Query("select * from student_result_entity order by name")
    suspend fun getAllResult(): List<QuizResultEntity>

}