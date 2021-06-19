package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.ServerCloudData
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(val serverUser: ServerUserRepo, val cloudData: ServerCloudData): ViewModel(){

    fun createUser(email: String, password: String, name: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = serverUser.registerUser(email, password, name)

    fun createFirestoreUser(email: String, name: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = cloudData.CreateFirestoreUser(name, email)
}