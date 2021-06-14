package com.maricoolsapps.adminpart.utils

import com.maricoolsapps.adminpart.room.RoomDaoImpl
import com.maricoolsapps.adminpart.room.RoomEntity

class QuizArrangementRepository constructor(var dao: RoomDaoImpl) {

    suspend fun addQuiz(quiz: RoomEntity){
            //insert the quiz to rom
            dao.insertQuiz(quiz)
    }

    suspend fun updateQuiz(quiz: RoomEntity){
        //insert the quiz to rom
        dao.updateQuiz(quiz)
    }
}