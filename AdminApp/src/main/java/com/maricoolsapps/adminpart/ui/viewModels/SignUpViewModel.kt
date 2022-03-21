package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.user.ServerUserRepo
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.SignInDetails
import com.maricoolsapps.utils.others.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(val serverUser: ServerUserRepo, val cloud: AdminCloudData) : ViewModel() {

    private val _logginIn = MutableLiveData<Boolean>()
    val logginIn: LiveData<Boolean> get() = _logginIn

    private val _success = MutableLiveData<String?>()
    val success: LiveData<String?> get() = _success

    fun signUser(name: String, email: String, password: String) {
        val details = SignInDetails(email, password)
        viewModelScope.launch {
            serverUser.registerUser(details) {
                when (it.status) {
                    Status.LOADING -> {
                        _logginIn.postValue(true)
                    }
                    Status.SUCCESS -> {
                        _logginIn.postValue(true)
                        getAuthState(name, email)
                    }
                    Status.ERROR -> {
                        _logginIn.postValue(false)
                    }
                }
            }
        }
    }

    private fun getAuthState(name: String, email: String) = serverUser.getAuthState().observeForever {
        if (it != null) {
            val user = AdminUser(it.uid, name, email, null)
            sendDetailsToDb(user)
        }
    }

    private fun sendDetailsToDb(user: AdminUser) {
        viewModelScope.launch(IO) {
            cloud.CreateFirestoreUser(user) {
                when (it.status) {
                    Status.SUCCESS -> {
                        _logginIn.postValue(false)
                        _success.postValue(it.data)
                    }
                    Status.ERROR -> {
                        _logginIn.postValue(false)
                    }
                    Status.LOADING -> {
                        _logginIn.postValue(true)
                    }
                }
            }
        }
    }
}