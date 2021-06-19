package com.maricoolsapps.adminpart.utils

import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.utils.MyDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SavedQuizRepository(val daoImpl: RoomDaoImpl) {

    suspend fun getListOfSavedQuiz(): Flow<com.maricoolsapps.utils.MyDataState> = flow{
        emit(com.maricoolsapps.utils.MyDataState.isLoading)
        try{
            emit(com.maricoolsapps.utils.MyDataState.onLoaded(daoImpl.getAllQuiz()))
        }catch (e: Exception){
            emit(com.maricoolsapps.utils.MyDataState.notLoaded(e))
        }
    }

    suspend fun deleteSavedQuiz(quiz: List<RoomEntity>){
        daoImpl.deleteQuiz(quiz)
    }
}