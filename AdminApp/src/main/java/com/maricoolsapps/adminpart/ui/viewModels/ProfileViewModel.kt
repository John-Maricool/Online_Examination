package com.maricoolsapps.adminpart.ui.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val serverUser: ServerUserRepo, val cloudData: AdminCloudData): ViewModel(){

    val profilePhoto: LiveData<String?> = cloudData.getProfileUri()

    fun updateProfileInFirestore(newPhoto: String): LiveData<MyServerDataState> = cloudData.updateProfileUri(newPhoto)

    fun changeProfilePhoto(newPhoto: Uri): LiveData<MyServerDataState> = serverUser.changeProfilePhoto(newPhoto)
}