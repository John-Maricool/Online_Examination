package com.maricoolsapps.studentapp.ui

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.studentapp.repos.MainQuizRepository
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.others.constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
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
    lateinit var itemClick: onTimeClick
    var questionCount = 1
    var currentQuestion: RoomEntity? = null
    var score = 0
    lateinit var countDownTimer: CountDownTimer
    var questionsSize = 0

    init {
            viewModelScope.launch(Main) {
                try {
                    val res = async(IO) { repository.allQuiz() }
                    val result = res.await()
                    if (res.isCompleted) {
                        questions = result
                        questionsSize = result.size
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

    fun sendQuizResult(score: Int) = repository.sendQuizResult(score)

    fun deactivateStudent() = repository.deactivateStudent()


    fun startTimer(listener: onTimeClick){
        itemClick = listener
        val timeToAnswer = constants.time
        if (timeToAnswer != null) {
            countDownTimer = object: CountDownTimer((timeToAnswer*1000*60).toLong(), 10){
                override fun onFinish() {
                    itemClick.onFinishSelected()
                }

                override fun onTick(p0: Long) {
                    itemClick.onTickSelected(p0, timeToAnswer)
                }
            }.start()
        }
    }

    interface onTimeClick{
        fun onTickSelected(p0: Long, ttA: Int?)
        fun onFinishSelected()
    }
}