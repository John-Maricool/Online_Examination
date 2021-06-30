package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.adminpart.utils.SavedQuizRepository
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedQuizViewModel
@Inject constructor(
        private val repository: SavedQuizRepository
): ViewModel() {

    var clicks = 0

    private val _dataState: MutableLiveData<MyDataState> = MutableLiveData()

    val dataState: LiveData<MyDataState> get() = _dataState

    fun start() {
        viewModelScope.launch {
            repository.getListOfSavedQuiz().onEach {
                _dataState.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun delete(quiz: List<RoomEntity>) {
        viewModelScope.launch {
            repository.deleteSavedQuiz(quiz)
        }
    }

    fun deleteQuiz(){
        viewModelScope.launch(IO) {
            repository.deleteQuiz()
        }
    }
    fun clearQuizDocs() = repository.clearQuizDocs()

    fun addToFirebase(data: Any): LiveData<MyServerDataState> = repository.addToFirebase(data)

    fun map(): List<ServerQuizDataModel> {
        return repository.map()
    }
}