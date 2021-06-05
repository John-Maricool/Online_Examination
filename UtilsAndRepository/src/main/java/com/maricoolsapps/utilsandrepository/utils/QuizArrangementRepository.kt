package com.maricoolsapps.utilsandrepository.utils

import com.maricoolsapps.localdatabase.room.RoomDao
import com.maricoolsapps.localdatabase.room.RoomEntity
import com.maricoolsapps.utilsandrepository.models.ServerQuizDataModel


class QuizArrangementRepository constructor(var dao: RoomDaoImpl) {

    suspend fun addQuiz(quiz: ServerQuizDataModel){
           val localQuiz =  dao.mapToLocalDatabase(quiz)
            dao.insertQuiz(localQuiz)
    }

    suspend fun updateQuiz(quiz: ServerQuizDataModel){
        //insert the quiz to rom
        val localQuiz =  dao.mapToLocalDatabase(quiz)

        dao.updateQuiz(localQuiz)
    }
}