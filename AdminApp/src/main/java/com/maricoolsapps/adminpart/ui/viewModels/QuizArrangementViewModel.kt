package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utilsandrepository.models.ServerQuizDataModel
import com.maricoolsapps.utilsandrepository.utils.QuizArrangementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizArrangementViewModel
 @Inject constructor(var repository: QuizArrangementRepository): ViewModel() {

    fun addQuiz(quiz: ServerQuizDataModel){
        viewModelScope.launch {
            //insert the quiz to rom
            repository.addQuiz(quiz)
        }
    }
    fun updateQuiz(quiz: ServerQuizDataModel){
        viewModelScope.launch {
            //insert the quiz to rom
            repository.updateQuiz(quiz)
        }
    }
}