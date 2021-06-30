package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.maricoolsapps.adminpart.utils.RegisteredUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisteredUsersViewModel
@Inject constructor(
        private val repository: RegisteredUsersRepository
): ViewModel() {
    fun start() = repository.getList()
    fun deleteStudents(ids: List<String>) = repository.deleteStudents(ids)

}