package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.SignInDetails
import com.maricoolsapps.utils.source.ServerUser
import com.maricoolsapps.utils.user.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel
    @Inject
    constructor(private val serverUser: ServerUserRepo, val cloud: StudentCloudData): ViewModel(){

    private val _changed = MutableLiveData<MyDataState<String>>()
    val changed: LiveData<MyDataState<String>> get() = _changed

    fun changeEmail(details: SignInDetails, email: String){
        viewModelScope.launch {
            serverUser.changeEmail(email, details)
            cloud.changeProfileEmail(serverUser.getUserUid(), email){
                _changed.postValue(it)
            }
        }
    }
    }