package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.LiveData
import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyServerDataState
import javax.inject.Inject

class UploadQuizRepository
@Inject constructor(
        val daoImpl: RoomDaoImpl,
        val adminCloudData: AdminCloudData
) {

    fun isQuizEmpty(): Boolean{
        return daoImpl.isQuizEmpty()
    }

    fun addToFirebase(data: Any): LiveData<MyServerDataState> = adminCloudData.addToFirebase(data)

    val deleteQuizDocs: LiveData<Boolean> = adminCloudData.deleteAllQuizDocs()

    suspend fun deleteQuiz(){
        daoImpl.deleteQuiz(daoImpl.getAllQuiz())
    }

     fun map(): List<ServerQuizDataModel>{
        return daoImpl.map()
    }
}





