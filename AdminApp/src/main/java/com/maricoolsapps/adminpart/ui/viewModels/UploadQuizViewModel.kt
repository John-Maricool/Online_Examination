package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.*
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.adminpart.utils.UploadQuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadQuizViewModel
@Inject constructor(
        val repo: UploadQuizRepository
): ViewModel(){

    var clicks = 0

    fun isQuizEmpty(): Boolean{
        return repo.isQuizEmpty()
    }

    fun deleteQuiz(){
        viewModelScope.launch {
            repo.deleteQuiz()
        }
    }

    val deleteQuizDocs: LiveData<Boolean> = repo.deleteQuizDocs


    fun addToFirebase(data: Any): LiveData<MyServerDataState> = repo.addToFirebase(data)

    fun map(): List<ServerQuizDataModel>{
      return repo.map()
    }

}