package com.maricoolsapps.room_library.room

class RoomDaoImpl(
    val dao: RoomDao, val mapper: CloudMapper
) {

    suspend fun insertQuiz(quiz: RoomEntity) = dao.insertQuiz(quiz)

    suspend fun insertQuiz(quiz: List<RoomEntity>) = dao.insertQuiz(quiz)

    suspend fun updateQuiz(quiz: RoomEntity) = dao.updateQuiz(quiz)

    suspend fun deleteQuiz(quiz: List<RoomEntity>) {
        quiz.forEach {
            dao.deleteQuiz(it)
        }
    }

    suspend fun getAllQuiz(): List<RoomEntity> = dao.getAllQuiz()

    suspend fun deleteResult(quiz: List<QuizResultEntity>) = dao.deleteResult(quiz)

    suspend fun getAllResult(): List<QuizResultEntity> = dao.getAllResult()

    suspend fun insertResult(quiz: QuizResultEntity) = dao.insertResult(quiz)

    fun isQuizEmpty(): Boolean {
        return dao.getAllQuiz().isEmpty()
    }

    fun map(): List<ServerQuizDataModel> {
        var cloud_quiz = listOf<ServerQuizDataModel>()
        val quiz = dao.getAllQuiz()
        if (!isQuizEmpty()) {
            cloud_quiz = mapper.convertToList(quiz)
        }
        return cloud_quiz
    }

    fun mapToRoom(data: List<ServerQuizDataModel>): List<RoomEntity> {
        var cloud_quiz = listOf<RoomEntity>()
        if (data.isNotEmpty()) {
            cloud_quiz = mapper.convertToRoomList(data)
        }
        return cloud_quiz
    }
}