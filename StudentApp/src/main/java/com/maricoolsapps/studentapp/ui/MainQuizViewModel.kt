package com.maricoolsapps.studentapp.ui

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.studentapp.repos.MainQuizRepository
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.others.constants
import com.maricoolsapps.utils.user.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainQuizViewModel
@Inject constructor(private val cloud: StudentCloudData, val user: ServerUserRepo) : ViewModel() {

    private val _questionsLive = MutableLiveData<MyDataState<List<String>>>()
    val questionsLive: LiveData<MyDataState<List<String>>> get() = _questionsLive

    val timeToAnswer = MutableLiveData<Int>()

    private val _question = MutableLiveData<MyDataState<DocumentSnapshot?>>()
    val question: LiveData<MyDataState<DocumentSnapshot?>> get() = _question

    private val _finish = MutableLiveData<MyDataState<String>>()
    val finish: LiveData<MyDataState<String>> get() = _finish

    var questionIndex = 0
    lateinit var itemClick: onTimeClick
    var questionCount = 1
    var score = 0
     var countDownTimer: CountDownTimer? = null

    init {
        viewModelScope.launch(IO) {
            cloud.getQuizTime(user.getUserUid()){
                timeToAnswer.postValue(it.data?.time)
            }
            cloud.getAllQuizDocId(user.getUserUid()) {
                _questionsLive.postValue(it)
            }
        }
    }

    fun getQuestion(questionId: String) {
        viewModelScope.launch(IO) {
            cloud.getQuiz(user.getUserUid(), questionId) {
                _question.postValue(it)
            }
        }
    }

    fun sendQuizResult() {
        val percentage = (score * 100).div(questionCount)
        viewModelScope.launch {
            cloud.sendQuizResult(percentage, user.getUserUid()){
                _finish.postValue(it)
            }
        }
    }
    fun startTimer(listener: onTimeClick) {
        itemClick = listener
        val time = timeToAnswer.value
        if (time != null) {
            countDownTimer = object : CountDownTimer(( time* 1000).toLong(), 10) {
                override fun onFinish() {
                    //this.start()
                    itemClick.onFinishSelected()
                }


                override fun onTick(p0: Long) {
                    itemClick.onTickSelected(p0, time)
                }
            }.start()
        }
    }


    interface onTimeClick {
        fun onTickSelected(p0: Long, ttA: Int?)
        fun onFinishSelected()
    }
}