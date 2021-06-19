package com.maricoolsapps.adminpart.utils

import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.utils.datastate.MyDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SavedQuizRepository(val daoImpl: RoomDaoImpl) {

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
}