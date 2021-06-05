package com.maricoolsapps.adminpart.ui.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utilsandrepository.utils.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val serverRepository: ServerRepository): ViewModel(){

    val profilePhoto = serverRepository.profilePhoto

    fun changeProfilePhoto(newPhoto: Uri) = serverRepository.changeProfilePhoto(newPhoto)
}