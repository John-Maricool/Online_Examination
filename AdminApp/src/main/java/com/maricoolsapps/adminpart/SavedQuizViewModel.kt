package com.maricoolsapps.adminpart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.adminpart.room.RoomEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedQuizViewModel
@Inject constructor(
        private val repository: SavedQuizRepository
): ViewModel() {

        private val _dataState: MutableLiveData<MyDataState<List<RoomEntity>>> = MutableLiveData()

    val dataState: LiveData<MyDataState<List<RoomEntity>>> get() = _dataState

    fun start(){
        viewModelScope.launch {
            repository.getListOfSavedQuiz().onEach {
                _dataState.value = it
            }
                    .launchIn(viewModelScope)
        }
    }
}