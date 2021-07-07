package com.maricoolsapps.studentapp.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObjects
import com.maricoolsapps.room_library.room.CloudMapper
import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyServerDataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class MainRepository
@Inject constructor(val cloud: StudentCloudData, val roomDaoImpl: RoomDaoImpl){

    fun registerForQuiz(id: String): LiveData<MyServerDataState>  = cloud.registerForQuiz(id)
    fun checkIfPreviouslyRegistered(): LiveData<Boolean> = cloud.checkIfPreviouslyRegistered()
    //fun checkIfAdminDocExist(id: String): LiveData<Boolean> = cloud.checkIfAdminDocExist(id)
    fun isTimeReached() = cloud.checkIfItsTimeToAccessQuiz()

    suspend fun insertToLocalDatabase(data: List<RoomEntity>) {
         roomDaoImpl.insertQuiz(data)
    }

    fun mapToLocalDb(data: List<ServerQuizDataModel>): List<RoomEntity>{
       return roomDaoImpl.mapToRoom(data)
    }

    fun getQuizFromServer() = cloud.downloadQuiz()

    suspend fun allQuiz(): Int{
        return roomDaoImpl.getAllQuiz().size
    }
}
