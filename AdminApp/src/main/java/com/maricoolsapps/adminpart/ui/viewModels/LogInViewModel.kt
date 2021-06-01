package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.maricoolsapps.adminpart.utils.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel
@Inject constructor(val serverUser: ServerUser): ViewModel(){

    val currentUser = serverUser.currentUser

    fun logInUser(email: String, password: String): Task<AuthResult> {
        return serverUser.signInUser(email, password)
    }
}