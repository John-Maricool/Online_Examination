package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.maricoolsapps.adminpart.utils.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel
@Inject constructor(private val serverUser: ServerUser): ViewModel(){

    fun reAuthenticate(oldPassowrd: String): Task<Void>?{
        return serverUser.reAuthenticate(oldPassowrd)
    }

    fun changePassword(newPassword: String): Task<Void>? {
        return serverUser.changePasword(newPassword)
    }
}