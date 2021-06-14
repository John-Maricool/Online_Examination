package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.maricoolsapps.adminpart.utils.MyServerDataState
import com.maricoolsapps.adminpart.utils.ServerUser
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel
@Inject constructor(private val serverUser: ServerUserRepo): ViewModel(){

    fun reAuthenticate(oldPassowrd: String): LiveData<MyServerDataState> = serverUser.reAuthenticate(oldPassowrd)
    fun changePassword(newPassword: String): LiveData<MyServerDataState> = serverUser.changePassword(newPassword)
}