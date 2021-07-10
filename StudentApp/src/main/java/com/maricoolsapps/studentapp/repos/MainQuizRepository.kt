package com.maricoolsapps.studentapp.repos

import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.RoomEntity
import javax.inject.Inject

class MainQuizRepository
@Inject constructor(val roomDaoImpl: RoomDaoImpl){

    suspend fun allQuiz(): List<RoomEntity> {
         return roomDaoImpl.getAllQuiz()
    }

    suspend fun deleteAllQuiz(quiz: List<RoomEntity>) {
         roomDaoImpl.deleteQuiz(quiz)
    }
}
