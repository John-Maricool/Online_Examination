package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.maricoolsapps.utilsandrepository.utils.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel
@Inject constructor(val serverRepository: ServerRepository): ViewModel(){

    val currentUser = serverRepository.currentUser

    fun logInUser(email: String, password: String) = serverRepository.logInUser(email, password)
}