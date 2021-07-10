package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObjects
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.studentapp.repos.MainRepository
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(private val repository: MainRepository): ViewModel() {

    fun registerForQuiz(id: String): LiveData<MyServerDataState> = repository.registerForQuiz(id)
    fun checkIfPreviouslyRegistered(): LiveData<Boolean> = repository.checkIfPreviouslyRegistered()
    fun isTimeReached() = repository.isTimeReached()
    // fun checkIfAdminDocExist(id: String): LiveData<Boolean> = repository.checkIfAdminDocExist(id)

    fun insertToLocalDatabase(): LiveData<Boolean> {
        val _data = MutableLiveData<Boolean>()
        viewModelScope.launch(IO) {
            val job = async (IO){ repository.getQuizFromServer() }

            job.await().let { state ->
                when (state) {
                    is MyDataState.onLoaded -> {
                        val snap = state.data as QuerySnapshot
                        val result = snap.toObjects<ServerQuizDataModel>()
                        val local_quiz = repository.mapToLocalDb(result)
                        withContext(Dispatchers.Default) { repository.insertToLocalDatabase(local_quiz) }.let {
                            _data.postValue(true)
                        }
                    }
                    MyDataState.isLoading -> TODO()
                    is MyDataState.notLoaded -> {
                        _data.postValue(false)
                    }
                }
            }
        }
        return _data
    }
}