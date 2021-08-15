package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.QuizResultEntity
import com.maricoolsapps.room_library.room.RoomDaoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedResultViewModel
    @Inject constructor(val dao: RoomDaoImpl): ViewModel() {

    private val _state = MutableLiveData<List<QuizResultEntity>>()
    val state: LiveData<List<QuizResultEntity>> get() = _state

        fun getAllResults() {
            viewModelScope.launch {
               _state.postValue(dao.getAllResult())
            }
        }

        fun deleteAll() {
             viewModelScope.launch {
                dao.deleteResult(dao.getAllResult())
            }
        }
}