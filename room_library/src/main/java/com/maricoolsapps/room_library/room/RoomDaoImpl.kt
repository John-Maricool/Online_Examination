package com.maricoolsapps.room_library.room

class RoomDaoImpl(val dao: RoomDao, val mapper: CloudMapper
) {

    suspend fun insertQuiz(quiz: RoomEntity) = dao.insertQuiz(quiz)

    suspend fun updateQuiz(quiz: RoomEntity) = dao.insertQuiz(quiz)

    suspend fun deleteQuiz(quiz: List<RoomEntity>) = dao.deleteQuiz(quiz)

    suspend fun getAllQuiz(): List<RoomEntity> = dao.getAllQuiz()

    fun isQuizEmpty(): Boolean{
        return dao.getAllQuiz().isEmpty()
    }

    fun map(): List<ServerQuizDataModel>{
        var cloud_quiz = listOf<ServerQuizDataModel>()
        val quiz = dao.getAllQuiz()
        if (!isQuizEmpty()){
            cloud_quiz = mapper.convertToList(quiz)
        }
        return cloud_quiz
    }
}