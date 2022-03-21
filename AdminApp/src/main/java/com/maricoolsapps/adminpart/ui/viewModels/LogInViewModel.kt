package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.user.ServerUserRepo
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.models.SignInDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel
@Inject constructor(val serverUser: ServerUserRepo) : ViewModel() {

    private val _result = MutableLiveData<MyDataState<String>>()
    val result: LiveData<MyDataState<String>> get() = _result

    fun logInUser(email: String, password: String) {
        val details = SignInDetails(email, password)
        viewModelScope.launch(IO) {
            serverUser.signInUser(details) {
                _result.postValue(it)
            }
        }
    }

}