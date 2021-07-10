package com.maricoolsapps.adminpart.utils

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.firebase.auth.*
import com.maricoolsapps.utils.user.ServerUser
import com.maricoolsapps.utils.datastate.MyServerDataState

class ServerUserRepo
  constructor(var user: ServerUser) {

    fun signInUser(email: String, password: String): LiveData<MyServerDataState> = user.signInUser(email, password)

    fun getUserName(): String? = user.getUserName()

    fun getProfilePhoto(): LiveData<Uri?> = user.getProfilePhoto()

    fun changeProfilePhoto(uri: Uri): LiveData<MyServerDataState> = user.changeProfilePhoto(uri)

    fun reAuthenticate(oldPassword: String): LiveData<MyServerDataState> = user.reAuthenticate(oldPassword)

    fun changeUsername(name: String): LiveData<MyServerDataState> = user.changeUsername(name)

    fun changePassword(newPassword: String): LiveData<MyServerDataState> = user.changePassword(newPassword)
}