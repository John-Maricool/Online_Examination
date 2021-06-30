package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.adminpart.utils.QuizArrangementRepository
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

    fun updateQuiz(quiz: RoomEntity){
        viewModelScope.launch {
            //insert the quiz to rom
            repository.updateQuiz(quiz)
        }
    }
}