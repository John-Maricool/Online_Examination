package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maricoolsapps.adminpart.room.RoomDaoImpl
import com.maricoolsapps.adminpart.utils.ServerCloudData
import com.maricoolsapps.adminpart.models.ServerQuizDataModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadQuizRepository
@Inject constructor(
        val daoImpl: RoomDaoImpl,
        val serverCloudData: ServerCloudData
) {

    fun isQuizEmpty(): Boolean{
        return daoImpl.isQuizEmpty()
    }

    fun addToFirebase(key: String, data: Any): LiveData<MyServerDataState> = serverCloudData.addToFirebase(key, data)

    suspend fun deleteQuiz(){
        daoImpl.deleteQuiz(daoImpl.getAllQuiz())
    }

     fun map(): List<ServerQuizDataModel>{
        return daoImpl.map()
    }
}





