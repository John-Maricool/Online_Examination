package com.maricoolsapps.studentapp

import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.models.StudentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject constructor(private val repository: SignUpRepository): ViewModel(){
    fun createUser(user: StudentUser) = repository.createUser(user)
}