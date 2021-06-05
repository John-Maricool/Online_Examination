package com.maricoolsapps.utilsandrepository.utils

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.maricoolsapps.serverdatabase.ServerUser
import javax.inject.Inject

class ServerRepository
@Inject constructor(private val user: ServerUser) {

    val signOut = user.signOut

    val currentUser = user.currentUser

    val currentName = user.getUserName()

    val profilePhoto = user.getProfilePhoto()

    fun changeProfilePhoto(newPhoto: Uri): Task<Void>? = user.changeProfilePhoto(newPhoto)

    fun changeName(name: String): Task<Void>? = user.changeUsername(name)

    fun reAuthenticate(oldPassword: String): Task<Void>?  = user.reAuthenticate(oldPassword)

    fun changePassword(newPassword: String): Task<Void>? = user.changePasword(newPassword)

    fun logInUser(email: String, password: String): Task<AuthResult> = user.signInUser(email, password)

    fun createUser(email: String, password: String): Task<AuthResult>  = user.registerUser(email, password)

    fun updateProfileName(name: String): Task<Void>? = user.updateProfileName(name)
}