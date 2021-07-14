package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel
    @Inject constructor(private val cloud: StudentCloudData): ViewModel() {

        fun getStudent(): LiveData<MyDataState> = cloud.getStudent()
 }