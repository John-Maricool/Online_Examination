package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.user.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val cloud: StudentCloudData, val user: ServerUserRepo) : ViewModel() {

    private val _register = MutableLiveData<MyDataState<String>>()
    val register: LiveData<MyDataState<String>> get() = _register

    private val _check = MutableLiveData<MyDataState<Boolean>>()
    val check: LiveData<MyDataState<Boolean>> get() = _check

    private val _ready = MutableLiveData<MyDataState<Boolean>>()
    val ready: LiveData<MyDataState<Boolean>> get() = _ready

    init {
        checkIfPreviouslyRegistered()
    }

    fun registerForQuiz(id: String) {
        viewModelScope.launch {
            cloud.registerForQuiz(id, user.getUserUid()) {
                _register.postValue(it)
            }
        }
    }

    fun checkIfPreviouslyRegistered() {
        viewModelScope.launch {
            cloud.checkIfPreviouslyRegistered(user.getUserUid()) {
                _check.postValue(it)
            }
        }
    }

    fun checkIfUserIsActivated(){
        viewModelScope.launch {
            cloud.checkIfItsTimeToAccessQuiz(user.getUserUid()){
                _ready.postValue(it)
            }
        }
    }
}