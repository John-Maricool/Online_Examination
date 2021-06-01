package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.adminpart.models.RegisteredUsersModel
import com.maricoolsapps.adminpart.utils.MyDataState
import com.maricoolsapps.adminpart.utils.RegisteredUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredUsersViewModel
@Inject constructor(
        private val repository: RegisteredUsersRepository
): ViewModel() {

    private val _dataState: MutableLiveData<MyDataState<MutableList<RegisteredUsersModel>>> = MutableLiveData()

    val dataState: LiveData<MyDataState<MutableList<RegisteredUsersModel>>> get() = _dataState

    fun start() {
        viewModelScope.launch {
            repository.getListOfRegisteredUsers().onEach {
                _dataState.value = it
            }
                    .launchIn(viewModelScope)
        }
    }
}