package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.studentapp.repos.MainRepository
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject constructor(private val repository: MainRepository): ViewModel(){

    fun registerForQuiz(id: String): LiveData<MyServerDataState> = repository.registerForQuiz(id)
    fun checkIfPreviouslyRegistered(): LiveData<Boolean> = repository.checkIfPreviouslyRegistered()
    fun isTimeReached() = repository.isTimeReached()
    fun checkIfAdminDocExist(id: String): LiveData<Boolean> = repository.checkIfAdminDocExist(id)

      fun insertToLocalDatabase(data: List<RoomEntity>): LiveData<Boolean> {
         val _data = MutableLiveData<Boolean>()
        val job =  viewModelScope.launch(IO) {
             repository.insertToLocalDatabase(data)
         }
          viewModelScope.launch(Main) {
              job.join()
              if (job.isCompleted){
                  _data.postValue(true)
              }else{
                  _data.postValue(false)
              }
          }
         return _data
     }

    fun mapToLocalDb(data: List<ServerQuizDataModel>) =  repository.mapToLocalDb(data)

    fun getQuizFromServer(): LiveData<MyDataState> = repository.getQuizFromServer()

    fun allQuizSize(): LiveData<Int>{
        val it = MutableLiveData<Int>()
        viewModelScope.launch {
           it.postValue(repository.allQuiz())
        }
        return it
    }
}