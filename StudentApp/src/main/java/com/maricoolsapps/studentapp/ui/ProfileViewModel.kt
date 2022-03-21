package com.maricoolsapps.studentapp.ui

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.source.ServerUser
import com.maricoolsapps.utils.user.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val user: ServerUserRepo, val cloud: StudentCloudData) : ViewModel() {

    private val _profilePhoto = MutableLiveData<MyDataState<String>>()
    val profilePhoto: LiveData<MyDataState<String>> get() = _profilePhoto

    private val _student = MutableLiveData<MyDataState<StudentUser>>()
    val student: LiveData<MyDataState<StudentUser>> get() = _student

    init {
        getAdmin()
    }

    fun changeProfilePhoto(newPhoto: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cloud.changeProfilePhoto(user.getUserUid(), newPhoto) {
                _profilePhoto.postValue(it)
            }
        }
    }

    private fun getAdmin() {
        viewModelScope.launch(Dispatchers.IO) {
            cloud.getStudent(user.getUserUid()) {
                _student.postValue(it)
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