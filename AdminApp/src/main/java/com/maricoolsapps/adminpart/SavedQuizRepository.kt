package com.maricoolsapps.adminpart

import androidx.lifecycle.LiveData
import com.maricoolsapps.adminpart.room.RoomDao
import com.maricoolsapps.adminpart.room.RoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavedQuizRepository
@Inject
constructor(val dao: RoomDao) {

    suspend fun getListOfSavedQuiz(): Flow<MyDataState<List<RoomEntity>>> = flow{
        emit(MyDataState.isLoading)
        try{
            emit(MyDataState.onLoaded(dao.getAllQuiz()))
        }catch (e: Exception){
            emit(MyDataState.notLoaded(e))
        }
    }
}