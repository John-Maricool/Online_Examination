package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.LiveData
import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.ServerCloudData
import javax.inject.Inject

class UploadQuizRepository
@Inject constructor(
        val daoImpl: RoomDaoImpl,
        val serverCloudData: ServerCloudData
) {

    fun isQuizEmpty(): Boolean{
        return daoImpl.isQuizEmpty()
    }

    fun addToFirebase(data: Any): LiveData<com.maricoolsapps.utils.MyServerDataState> = serverCloudData.addToFirebase(data)

    suspend fun deleteQuiz(){
        daoImpl.deleteQuiz(daoImpl.getAllQuiz())
    }

     fun map(): List<ServerQuizDataModel>{
        return daoImpl.map()
    }
}





