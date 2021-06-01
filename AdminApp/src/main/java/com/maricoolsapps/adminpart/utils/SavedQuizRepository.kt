package com.maricoolsapps.adminpart.utils

import com.maricoolsapps.adminpart.room.RoomDaoImpl
import com.maricoolsapps.adminpart.room.RoomEntity
import com.maricoolsapps.adminpart.utils.MyDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavedQuizRepository
@Inject
constructor(val daoImpl: RoomDaoImpl) {

    suspend fun getListOfSavedQuiz(): Flow<MyDataState<List<RoomEntity>>> = flow{
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