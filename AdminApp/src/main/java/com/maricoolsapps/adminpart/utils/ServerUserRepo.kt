package com.maricoolsapps.adminpart.utils

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.firebase.auth.*
import com.maricoolsapps.utils.ServerUser

class ServerUserRepo
  constructor(var user: ServerUser) {

     val currentUser: FirebaseUser? = user.currentUser

    fun getUserId(): String? = user.getUserId()

    fun registerUser(email: String, password: String, name: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = user.registerUser(email, password, name)

    fun signInUser(email: String, password: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = user.signInUser(email, password)

    fun updateProfileName(name: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = user.updateProfileName(name)

    private fun getUserEmail(): String? = user.getUserEmail()

    fun getUserName(): String? = user.getUserName()

    fun getProfilePhoto(): LiveData<Uri?> = user.getProfilePhoto()


    fun changeProfilePhoto(uri: Uri): LiveData<com.maricoolsapps.utils.MyServerDataState> = user.changeProfilePhoto(uri)

    fun reAuthenticate(oldPassword: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = user.reAuthenticate(oldPassword)

    fun changeUsername(name: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = user.changeUsername(name)

    fun changePassword(newPassword: String): LiveData<com.maricoolsapps.utils.MyServerDataState> = user.changePassword(newPassword)
}