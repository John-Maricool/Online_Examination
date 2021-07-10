package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.user.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentLogInViewModel
    @Inject constructor(val user: ServerUser): ViewModel() {
    fun logInUser(email: String, password: String): LiveData<MyServerDataState> = user.signInUser(email, password)

}