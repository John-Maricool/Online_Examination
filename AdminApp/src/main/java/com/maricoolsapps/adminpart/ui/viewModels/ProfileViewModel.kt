package com.maricoolsapps.adminpart.ui.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.ServerCloudData
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val serverUser: ServerUserRepo, val cloudData: ServerCloudData): ViewModel(){

    val profilePhoto: LiveData<String?> = cloudData.getProfileUri()

    fun updateProfileInFirestore(newPhoto: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = cloudData.updateProfileUri(newPhoto)

    fun changeProfilePhoto(newPhoto: Uri): LiveData<com.maricoolsapps.utils.MyServerDataState> = serverUser.changeProfilePhoto(newPhoto)
}