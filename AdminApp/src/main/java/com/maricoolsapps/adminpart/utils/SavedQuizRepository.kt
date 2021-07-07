package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.LiveData
import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.QuizSettingModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavedQuizRepository
@Inject constructor(val daoImpl: RoomDaoImpl, val adminCloudData: AdminCloudData
) {

    suspend fun getListOfSavedQuiz(): Flow<MyDataState> = flow{
        emit(MyDataState.isLoading)
        try{
            emit(MyDataState.onLoaded(daoImpl.getAllQuiz()))
        }catch (e: Exception){
            emit(MyDataState.notLoaded(e))
        }
    }

    suspend fun deleteSavedQuiz(quiz: List<RoomEntity>){
        daoImpl.deleteQuiz(quiz)
    }

    fun addToFirebase(data: Any): LiveData<MyServerDataState> = adminCloudData.addToFirebase(data)

    suspend fun deleteQuiz(){
        daoImpl.deleteQuiz(daoImpl.getAllQuiz())
    }

    fun clearQuizDocs() = adminCloudData.deleteAllQuizDocs()

    fun map(): List<ServerQuizDataModel>{
        return daoImpl.map()
    }

    fun uploadQuizSettings(settings: QuizSettingModel) = adminCloudData.quizSetting(settings)

}