package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.maricoolsapps.utilsandrepository.utils.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(val serverRepository: ServerRepository): ViewModel(){

    fun createUser(email: String, password: String) = serverRepository.createUser(email, password)

    fun updateProfileName(name: String) = serverRepository.updateProfileName(name)
}