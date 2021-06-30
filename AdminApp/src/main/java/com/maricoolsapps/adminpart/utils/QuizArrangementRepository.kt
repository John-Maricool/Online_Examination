package com.maricoolsapps.adminpart.utils

import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.RoomEntity

class QuizArrangementRepository constructor(var dao: RoomDaoImpl) {

    suspend fun addQuiz(quiz: RoomEntity){
            //insert the quiz to room
            dao.insertQuiz(quiz)
    }

    suspend fun updateQuiz(quiz: RoomEntity){
        //insert the quiz to rom
        dao.updateQuiz(quiz)
    }
}