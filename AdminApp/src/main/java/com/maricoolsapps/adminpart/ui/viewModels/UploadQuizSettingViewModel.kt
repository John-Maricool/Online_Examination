package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.adminpart.utils.SavedQuizRepository
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.QuizSettingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadQuizSettingViewModel
@Inject constructor(
        private val repository: SavedQuizRepository
): ViewModel() {

    var clicks = 0

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

    fun uploadQuizSettings(settings: QuizSettingModel): LiveData<Boolean> = repository.uploadQuizSettings(settings)

}