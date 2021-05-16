package com.maricoolsapps.adminpart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.adminpart.room.RoomEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizArrangementViewModel
 @Inject constructor(var repository: QuizArrangementRepository): ViewModel() {

    fun addQuiz(quiz: RoomEntity){
        viewModelScope.launch {
            //insert the quiz to rom
            repository.addQuiz(quiz)
        }
    }
}