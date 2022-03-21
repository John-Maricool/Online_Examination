package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.LiveData
import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.QuizSettingModel
import com.maricoolsapps.utils.source.ServerUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavedQuizRepository
@Inject constructor(
    val user: ServerUser, val daoImpl: RoomDaoImpl, val adminCloudData: AdminCloudData
) {

    suspend fun getListOfSavedQuiz(): Flow<MyDataState<List<RoomEntity>>> = flow {
        emit(MyDataState.loading())
        try {
            emit(MyDataState.success(daoImpl.getAllQuiz()))
        } catch (e: Exception) {
            emit(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun deleteSavedQuiz(quiz: List<RoomEntity>) {
        daoImpl.deleteQuiz(quiz)
    }

    suspend fun addToFirebase(data: List<Any>, time: Int, result: (MyDataState<String>) -> Unit) {
        val setting = QuizSettingModel(time)
        adminCloudData.addToFirebase(user.getUserId(), data, setting) {
            result.invoke(it)
        }
    }

    suspend fun deleteQuiz() {
        daoImpl.deleteQuiz(daoImpl.getAllQuiz())
    }

    suspend fun addOverriteToDb(data: List<Any>, time: Int, result: (MyDataState<String>) -> Unit) {
        val setting = QuizSettingModel(time)
        adminCloudData.addOverriteToDb(user.getUserId(), data, setting) {
            result.invoke(it)
        }
    }

    fun map(): List<ServerQuizDataModel> {
        return daoImpl.map()
    }
}