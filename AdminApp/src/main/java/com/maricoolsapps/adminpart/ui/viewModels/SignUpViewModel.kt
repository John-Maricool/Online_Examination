package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(val serverUser: ServerUserRepo, val cloudData: AdminCloudData): ViewModel(){

    fun createUser(email: String, password: String, name: String): LiveData<MyServerDataState> = serverUser.registerUser(email, password, name)

    fun createFirestoreUser(email: String, name: String): LiveData<MyServerDataState> = cloudData.CreateFirestoreUser(name, email)
}