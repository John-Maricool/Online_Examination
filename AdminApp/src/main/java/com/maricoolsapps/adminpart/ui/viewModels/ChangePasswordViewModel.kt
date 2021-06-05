package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.maricoolsapps.utilsandrepository.utils.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel
@Inject constructor(private val serverRepo: ServerRepository): ViewModel(){

    fun reAuthenticate(oldPassowrd: String) = serverRepo.reAuthenticate(oldPassowrd)
    fun changePassword(newPassword: String) = serverRepo.changePassword(newPassword)
}