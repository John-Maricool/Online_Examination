package com.maricoolsapps.adminpart.utils

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServerUserRepo
@Inject
 constructor(var user: ServerUser) {

     val currentUser: FirebaseUser? = user.currentUser

    fun getUserId(): String? = user.getUserId()

    fun registerUser(email: String, password: String): LiveData<MyServerDataState> = user.registerUser(email, password)

    fun signInUser(email: String, password: String): LiveData<MyServerDataState> = user.signInUser(email, password)

    fun updateProfileName(name: String): LiveData<MyServerDataState> = updateProfileName(name)



    private fun getUserEmail(): String? = user.getUserEmail()

    fun getUserName(): String? = user.getUserName()

    fun getProfilePhoto(): Uri? = user.getProfilePhoto()

    fun changeProfilePhoto(uri: Uri): LiveData<MyServerDataState> = user.changeProfilePhoto(uri)

    fun reAuthenticate(oldPassword: String): LiveData<MyServerDataState> = user.reAuthenticate(oldPassword)

    fun changeUsername(name: String): LiveData<MyServerDataState> = user.changeUsername(name)

    fun changePassword(newPassword: String): LiveData<MyServerDataState> = user.changePassword(newPassword)
}