package com.maricoolsapps.studentapp.repos

import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import javax.inject.Inject

class MainQuizRepository
@Inject constructor(val roomDaoImpl: RoomDaoImpl, val cloud: StudentCloudData){

    /*suspend fun allQuiz(): List<RoomEntity> {
         return roomDaoImpl.getAllQuiz()
    }

    suspend fun deleteAllQuiz(quiz: List<RoomEntity>) {
         roomDaoImpl.deleteQuiz(quiz)
    }
*/
    /*fun sendQuizResult(score: Int) = cloud.sendQuizResult(score)

    fun deactivateStudent() = cloud.deactivateStudent()*/
}
