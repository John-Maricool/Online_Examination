package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.SignInDetails
import com.maricoolsapps.utils.source.ServerUser
import com.maricoolsapps.utils.user.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentLogInViewModel
    @Inject constructor(val user: ServerUserRepo): ViewModel() {

    private val _result = MutableLiveData<MyDataState<String>>()
    val result: LiveData<MyDataState<String>> get() = _result

    fun logInUser(email: String, password: String) {
        val details = SignInDetails(email, password)
        viewModelScope.launch(Dispatchers.IO) {
            user.signInUser(details) {
                _result.postValue(it)
            }
        }
    }}