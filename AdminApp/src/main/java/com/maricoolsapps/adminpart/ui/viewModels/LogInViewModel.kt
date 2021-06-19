package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.MyServerDataState
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel
@Inject constructor(val serverUser: ServerUserRepo): ViewModel(){

    val currentUser = serverUser.currentUser

    fun logInUser(email: String, password: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = serverUser.signInUser(email, password)

}