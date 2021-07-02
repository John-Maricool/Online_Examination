package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.studentapp.repos.MainRepository
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(private val repository: MainRepository): ViewModel(){

    fun registerForQuiz(id: String): LiveData<MyServerDataState> = repository.registerForQuiz(id)
    fun checkIfPreviouslyRegistered(): LiveData<Boolean> = repository.checkIfPreviouslyRegistered()
    fun checkIfAdminDocExist(id: String): LiveData<Boolean> = repository.checkIfAdminDocExist(id)
}