package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utilsandrepository.models.RegisteredUsersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredUsersViewModel
@Inject constructor(
        //private val repository: RegisteredUsersRepository
): ViewModel() {

    /*private val _dataState: MutableLiveData<com.maricoolsapps.utilsandrepository.utils.MyDataState<MutableList<RegisteredUsersModel>>> = MutableLiveData()

    val dataState: LiveData<com.maricoolsapps.utilsandrepository.utils.MyDataState<MutableList<RegisteredUsersModel>>> get() = _dataState

    fun start() {
        viewModelScope.launch {
            repository.getListOfRegisteredUsers().onEach {
                _dataState.value = it
            }
                    .launchIn(viewModelScope)
        }
    }*/
}