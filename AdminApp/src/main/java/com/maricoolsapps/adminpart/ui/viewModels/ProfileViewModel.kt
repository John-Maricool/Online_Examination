package com.maricoolsapps.adminpart.ui.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.maricoolsapps.adminpart.utils.MyServerDataState
import com.maricoolsapps.adminpart.utils.ServerUser
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val serverUser: ServerUserRepo): ViewModel(){

    val profilePhoto = serverUser.getProfilePhoto()

    fun changeProfilePhoto(newPhoto: Uri): LiveData<MyServerDataState> = serverUser.changeProfilePhoto(newPhoto)

}