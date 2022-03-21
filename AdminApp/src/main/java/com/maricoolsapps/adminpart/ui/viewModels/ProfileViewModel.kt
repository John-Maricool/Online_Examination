package com.maricoolsapps.adminpart.ui.viewModels

import android.content.Intent
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.user.ServerUserRepo
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.models.AdminUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val user: ServerUserRepo, val cloud: AdminCloudData) : ViewModel() {

    private val _profilePhoto = MutableLiveData<MyDataState<String>>()
    val profilePhoto: LiveData<MyDataState<String>> get() = _profilePhoto

    private val _admin = MutableLiveData<MyDataState<AdminUser>>()
    val admin: LiveData<MyDataState<AdminUser>> get() = _admin

    init {
        getAdmin()
    }
    fun changeProfilePhoto(newPhoto: String) {
        viewModelScope.launch(IO) {
            cloud.changeProfilePhoto(user.getUserUid(), newPhoto){
                _profilePhoto.postValue(it)
            }
        }
    }

    private fun getAdmin() {
        viewModelScope.launch(IO) {
            cloud.getAdmin(user.getUserUid()){
                _admin.postValue(it)
            }
        }
    }

    fun chooseImage(): Intent {
        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        return intent
    }
}