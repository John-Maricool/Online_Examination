package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.adminpart.utils.RegisteredUsersRepository
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisteredUsersViewModel
@Inject constructor(
        private val repository: RegisteredUsersRepository
): ViewModel() {
    fun start() = repository.getList()
    fun deleteStudents(ids: List<String>) = repository.deleteStudents(ids)

    fun activateStudents(): LiveData<MyServerDataState> = repository.activateStudents()
    fun deactivateStudents(): LiveData<MyServerDataState> = repository.deactivateStudents()

}