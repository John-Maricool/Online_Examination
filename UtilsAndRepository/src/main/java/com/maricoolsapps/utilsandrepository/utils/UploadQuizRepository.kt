package com.maricoolsapps.utilsandrepository.utils

import com.maricoolsapps.serverdatabase.ServerCloudData
import com.maricoolsapps.utilsandrepository.models.ServerQuizDataModel
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
        return daoImpl.mapToServerList()
    }
}





