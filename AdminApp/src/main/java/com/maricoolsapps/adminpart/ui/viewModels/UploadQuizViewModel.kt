package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.*
import com.maricoolsapps.utilsandrepository.models.ServerQuizDataModel
import com.maricoolsapps.utilsandrepository.utils.UploadQuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadQuizViewModel
@Inject constructor(
        val repo: com.maricoolsapps.utilsandrepository.utils.UploadQuizRepository
): ViewModel(){

    val docRef = repo.docRef
    var clicks = 0


    fun isQuizEmpty(): Boolean{
        return repo.isQuizEmpty()
    }

    fun deleteQuiz(){
        viewModelScope.launch {
            repo.deleteQuiz()
        }
    }

    fun map(): List<ServerQuizDataModel>{
      return repo.map()
    }

}