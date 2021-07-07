package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.user.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(val serverUser: ServerUser, val cloud: AdminCloudData): ViewModel(){

    fun sendToFirestore(user: AdminUser, auth: FirebaseAuth): LiveData<MyServerDataState> = cloud.CreateFirestoreUser(user, auth)
    fun createUser(email: String, password: String, name: String): LiveData<MyServerDataState> = serverUser.registerUser(email, password, name)
}