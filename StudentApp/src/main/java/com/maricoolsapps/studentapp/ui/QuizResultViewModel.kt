package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.QuizResultEntity
import com.maricoolsapps.room_library.room.RoomDaoImpl
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel
    @Inject constructor(private val cloud: StudentCloudData, val dao: RoomDaoImpl): ViewModel() {

        fun getStudent(): LiveData<MyDataState> = cloud.getStudent()

    fun addResult(result: QuizResultEntity): Job =  viewModelScope.launch {
        dao.insertResult(result)
    }
 }