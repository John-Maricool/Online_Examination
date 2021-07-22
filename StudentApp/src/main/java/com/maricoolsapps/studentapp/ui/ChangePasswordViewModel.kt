package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.user.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel
    @Inject
    constructor(private val serverUser: ServerUser): ViewModel(){

        fun reAuthenticate(oldPassowrd: String): LiveData<MyServerDataState> = serverUser.reAuthenticate(oldPassowrd)
        fun changePassword(newPassword: String): LiveData<MyServerDataState> = serverUser.changePassword(newPassword)
    }