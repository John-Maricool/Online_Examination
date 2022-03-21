package com.maricoolsapps.studentapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.SignInDetails
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.source.ServerUser
import com.maricoolsapps.utils.user.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(val serverUser: ServerUserRepo, val cloud: StudentCloudData) : ViewModel() {

    private val _logginIn = MutableLiveData<Boolean>()
    val logginIn: LiveData<Boolean> get() = _logginIn

    private val _success = MutableLiveData<String?>()
    val success: LiveData<String?> get() = _success

    fun signUser(user: StudentUser, password: String) {
        val details = SignInDetails(user.email, password)
        viewModelScope.launch {
            serverUser.registerUser(details) {
                when (it.status) {
                    Status.LOADING -> {
                        _logginIn.postValue(true)
                    }
                    Status.SUCCESS -> {
                        _logginIn.postValue(true)
                        getAuthState(user)
                    }
                    Status.ERROR -> {
                        _logginIn.postValue(false)
                    }
                }
            }
        }
    }

    private fun getAuthState(user: StudentUser) {
        serverUser.getAuthState().observeForever {
            if (it != null) {
                user.id = it.uid
            }
            sendDetailsToDb(user)
        }
    }

    private fun sendDetailsToDb(user: StudentUser) {
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