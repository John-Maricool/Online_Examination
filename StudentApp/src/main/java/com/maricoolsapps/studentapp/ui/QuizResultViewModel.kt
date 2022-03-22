package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.QuizResultEntity
import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.user.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel
@Inject constructor(
    private val cloud: StudentCloudData,
    val dao: RoomDaoImpl,
    val user: ServerUserRepo
) : ViewModel() {

    private val _result = MutableLiveData<MyDataState<StudentUser>>()
    val result: LiveData<MyDataState<StudentUser>> get() = _result

    init {
        getStudent()
    }

    fun getStudent() {
        viewModelScope.launch {
            cloud.getStudent(user.getUserUid()) {
                _result.postValue(it)
            }
        }
    }

    fun addResult(result: QuizResultEntity): Job = viewModelScope.launch {
        dao.insertResult(result)
    }
}