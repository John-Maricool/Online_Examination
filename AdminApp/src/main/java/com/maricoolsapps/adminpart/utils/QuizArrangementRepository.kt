package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.adminpart.room.RoomDao
import com.maricoolsapps.adminpart.room.RoomEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject


class QuizArrangementRepository constructor(var dao: RoomDao) {

    suspend fun addQuiz(quiz: RoomEntity){
            //insert the quiz to rom
            dao.insertQuiz(quiz)
    }


    suspend fun updateQuiz(quiz: RoomEntity){
        //insert the quiz to rom
        dao.updateQuiz(quiz)
    }
}