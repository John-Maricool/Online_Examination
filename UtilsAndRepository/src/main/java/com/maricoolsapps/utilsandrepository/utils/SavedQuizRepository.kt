package com.maricoolsapps.utilsandrepository.utils

import com.maricoolsapps.utilsandrepository.models.ServerQuizDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SavedQuizRepository
constructor(val daoImpl: RoomDaoImpl) {

    suspend fun getListOfSavedQuiz(): Flow<MyDataState<List<ServerQuizDataModel>>> = flow{
        emit(MyDataState.isLoading)
        try{
            emit(MyDataState.onLoaded(daoImpl.mapToServerList()))
        }catch (e: Exception){
            emit(MyDataState.notLoaded(e))
        }
    }

    suspend fun deleteSavedQuiz(quiz: List<ServerQuizDataModel>){
        val localQuiz = daoImpl.mapToLocalDatabase(quiz)
        daoImpl.deleteQuiz(localQuiz)
    }
}