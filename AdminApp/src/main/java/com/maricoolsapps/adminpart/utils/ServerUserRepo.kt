package com.maricoolsapps.adminpart.utils

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.firebase.auth.*
import com.maricoolsapps.utils.user.ServerUser
import com.maricoolsapps.utils.datastate.MyServerDataState

class ServerUserRepo
  constructor(var user: ServerUser) {

  fun getUserEmail() = user.getUserEmail()

    fun signInUser(email: String, password: String): LiveData<MyServerDataState> = user.signInUser(email, password)

    fun getUserName(): String? = user.getUserName()

    suspend fun changeEmail(mail: String) = user.changeEmail(mail)

    fun getProfilePhoto(): LiveData<Uri?> = user.getProfilePhoto()

   suspend fun changeProfilePhoto(uri: Uri) = user.changeProfilePhoto(uri)

    fun reAuthenticate(oldPassword: String): LiveData<MyServerDataState> = user.reAuthenticate(oldPassword)

    suspend fun changeUsername(name: String)  = user.changeUsername(name)

    fun changePassword(newPassword: String): LiveData<MyServerDataState> = user.changePassword(newPassword)
}