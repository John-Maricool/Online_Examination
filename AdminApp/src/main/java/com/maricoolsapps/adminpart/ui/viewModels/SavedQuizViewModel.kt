package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.adminpart.utils.SavedQuizRepository
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

    private val _dataState: MutableLiveData<MyDataState> = MutableLiveData()

    val dataState: LiveData<MyDataState> get() = _dataState

    fun start() {
        viewModelScope.launch {
            repository.getListOfSavedQuiz().onEach {
                _dataState.value = it
            }
                    .launchIn(viewModelScope)
        }
    }

    fun delete(quiz: List<RoomEntity>) {
        viewModelScope.launch {
            repository.deleteSavedQuiz(quiz)
        }
    }
}