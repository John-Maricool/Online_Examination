package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.studentapp.repos.MainQuizRepository
import com.maricoolsapps.utils.datastate.MyDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainQuizViewModel
    @Inject constructor(private val repository: MainQuizRepository): ViewModel() {

   private val _questionsLive = MutableLiveData<MyDataState>()
    val questionsLive: LiveData<MyDataState> get() = _questionsLive

    var questions: List<RoomEntity>? = null
    var questionIndex = 0
    var questionCount = 1
    var currentQuestion: RoomEntity? = null
    var score = 0
    var isAnswered = false
    var questionsSize = 0

    init {
            viewModelScope.launch(Main) {
                try {
                    val res = async { repository.allQuiz() }
                    val result = res.await()
                    if (res.isCompleted) {
                        _questionsLive.postValue(MyDataState.onLoaded(result))
                    }else{
                        _questionsLive.postValue(MyDataState.notLoaded(Exception("Error")))
                    }
                }
                catch (e: Exception){
                    _questionsLive.postValue(MyDataState.notLoaded(e))
                }
            }
        }

    fun deleteQuiz(){
        viewModelScope.launch(Main) {
            repository.deleteAllQuiz(questions!!)
        }
    }
}