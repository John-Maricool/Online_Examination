package com.maricoolsapps.adminpart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.adminpart.room.RoomDao
import com.maricoolsapps.adminpart.room.RoomEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadQuizViewModel
@Inject constructor(
        val dao: RoomDao,
        val mapper: CloudMapper
): ViewModel(){

    lateinit var cloud_quiz:  List<ServerQuizData>

    fun isQuizEmpty(): Boolean{
        val quiz = dao.getAllQuiz()
        return quiz.isEmpty()
    }

    fun deleteQuiz(){
        viewModelScope.launch {
            dao.deleteQuiz(dao.getAllQuiz())
        }
    }

    fun map(): List<ServerQuizData>{
        val quiz = dao.getAllQuiz()
        if (quiz.isNotEmpty()){
           cloud_quiz = mapper.convertToList(quiz)
        }
        return cloud_quiz
    }
}