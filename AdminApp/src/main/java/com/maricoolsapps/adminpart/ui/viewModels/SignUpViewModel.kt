package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.maricoolsapps.adminpart.utils.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(val serverUser: ServerUser): ViewModel(){

    fun createUser(email: String, password: String): Task<AuthResult> {
        return serverUser.registerUser(email, password)
    }

    fun updateProfileName(name: String): Task<Void>?{
        return serverUser.updateProfileName(name)
    }
}