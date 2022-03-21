package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.source.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChangeProfileDialogViewModel
@Inject constructor(val serverUser: ServerUser, val cloud: AdminCloudData) : ViewModel() {

    private val _state = MutableLiveData<MyDataState<String>>()
    val state: LiveData<MyDataState<String>> get() = _state

    fun changeName(newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cloud.changeProfileName(serverUser.getUserId(), newName) {
                _state.postValue(it)
            }
        }
    }
}