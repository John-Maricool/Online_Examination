package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.*
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.adminpart.utils.SavedQuizRepository
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedQuizViewModel
@Inject constructor(
    private val repository: SavedQuizRepository
) : ViewModel() {

    private val _dataState: MutableLiveData<MyDataState<List<RoomEntity>>> = MutableLiveData()
    val dataState: LiveData<MyDataState<List<RoomEntity>>> get() = _dataState

    private val _add: MutableLiveData<String> = MutableLiveData()
    val add: LiveData<String> get() = _add

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> get() = _loading

    init {
        start()
    }

    private fun start() {
        viewModelScope.launch {
            repository.getListOfSavedQuiz().onEach {
                _dataState.postValue(it)
            }.launchIn(viewModelScope)
        }
    }

    fun delete(quiz: List<RoomEntity>) {
        viewModelScope.launch(Main) {
            repository.deleteSavedQuiz(quiz)
        }
    }

    fun deleteQuiz() {
        viewModelScope.launch(IO) {
            repository.deleteQuiz()
        }
    }

    fun addOverriteToDb(data: List<Any>, time: Int) {
        viewModelScope.async {
            repository.addOverriteToDb(data, time) {
                when (it.status) {
                    Status.SUCCESS -> {
                        deleteQuiz()
                        _loading.postValue(false)
                        _add.postValue(it.data)
                    }
                    Status.ERROR -> {
                        _loading.postValue(false)
                        _add.postValue(it.message)
                    }
                    Status.LOADING -> {
                        _loading.postValue(true)
                        _add.postValue(it.data)
                    }
                }
            }
        }
    }

    fun addToFirebase(data: List<Any>, time: Int) {
        viewModelScope.launch {
            repository.addToFirebase(data, time) {
                when (it.status) {
                    Status.SUCCESS -> {
                        deleteQuiz()
                        _loading.postValue(false)
                        _add.postValue(it.data)
                    }
                    Status.ERROR -> {
                        _loading.postValue(false)
                        _add.postValue(it.message)
                    }
                    Status.LOADING -> {
                        _loading.postValue(true)
                        _add.postValue(it.data)
                    }
                }
            }
        }
    }

   /* fun map(): List<ServerQuizDataModel> {
        return repository.map()
    }*/
}