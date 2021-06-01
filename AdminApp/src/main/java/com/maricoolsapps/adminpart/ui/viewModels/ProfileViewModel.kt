package com.maricoolsapps.adminpart.ui.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.maricoolsapps.adminpart.utils.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val serverUser: ServerUser): ViewModel(){

    val profilePhoto = serverUser.getProfilePhoto()

    fun changeProfilePhoto(newPhoto: Uri): Task<Void>? {
        return serverUser.changeProfilePhoto(newPhoto)
    }
}