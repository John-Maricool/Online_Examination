package com.maricoolsapps.utilsandrepository.utils

import com.maricoolsapps.localdatabase.room.RoomDao
import com.maricoolsapps.localdatabase.room.RoomEntity
import com.maricoolsapps.utilsandrepository.models.ServerQuizDataModel
import javax.inject.Inject

class RoomDaoImpl

@Inject
constructor(val dao: RoomDao, val mapper: CloudMapper,
) {

    suspend fun insertQuiz(quiz: RoomEntity) = dao.insertQuiz(quiz)

    suspend fun updateQuiz(quiz: RoomEntity) = dao.insertQuiz(quiz)

    suspend fun deleteQuiz(quiz: List<RoomEntity>) = dao.deleteQuiz(quiz)

    suspend fun getAllQuiz(): List<RoomEntity> = dao.getAllQuiz()

    fun isQuizEmpty(): Boolean{
        return dao.getAllQuiz().isEmpty()
    }

    fun mapToServerList(): List<ServerQuizDataModel>{
        var cloud_quiz = listOf<ServerQuizDataModel>()
        val quiz = dao.getAllQuiz()
        if (!isQuizEmpty()){
            cloud_quiz = mapper.convertToServerList(quiz)
        }
        return cloud_quiz
    }

    fun mapToLocalDatabase(item: ServerQuizDataModel): RoomEntity{
        return mapper.mapFromModel(item)
    }

    fun mapToLocalDatabase(item: List<ServerQuizDataModel>): List<RoomEntity>{
        return mapper.convertToLocalList(item)
    }

}