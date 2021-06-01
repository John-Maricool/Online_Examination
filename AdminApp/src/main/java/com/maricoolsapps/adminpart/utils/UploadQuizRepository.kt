package com.maricoolsapps.adminpart.utils

import com.maricoolsapps.adminpart.room.RoomDaoImpl
import com.maricoolsapps.adminpart.utils.ServerCloudData
import com.maricoolsapps.adminpart.models.ServerQuizDataModel
import javax.inject.Inject

class UploadQuizRepository
@Inject constructor(
        val daoImpl: RoomDaoImpl,
        val serverCloudData: ServerCloudData
) {

    fun isQuizEmpty(): Boolean{
        return daoImpl.isQuizEmpty()
    }

    val docRef = serverCloudData.docRef

    suspend fun deleteQuiz(){
        daoImpl.deleteQuiz(daoImpl.getAllQuiz())
    }

     fun map(): List<ServerQuizDataModel>{
        return daoImpl.map()
    }
}





