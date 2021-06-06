package com.maricoolsapps.utilsandrepository.utils

import android.net.Uri
import com.maricoolsapps.serverdatabase.ServerUser
import javax.inject.Inject

class ServerRepository
@Inject constructor(private val user: ServerUser) {

    val signOut = user.signOut

    val currentUser = user.currentUser

    val currentName = user.getUserName()

    val profilePhoto = user.getProfilePhoto()

    fun changeProfilePhoto(newPhoto: Uri) = user.changeProfilePhoto(newPhoto)

    fun changeName(name: String) = user.changeUsername(name)

    fun reAuthenticate(oldPassword: String) = user.reAuthenticate(oldPassword)

    fun changePassword(newPassword: String)= user.changePasword(newPassword)

    fun logInUser(email: String, password: String) = user.signInUser(email, password)

    fun createUser(email: String, password: String)  = user.registerUser(email, password)

    fun updateProfileName(name: String) = user.updateProfileName(name)
}