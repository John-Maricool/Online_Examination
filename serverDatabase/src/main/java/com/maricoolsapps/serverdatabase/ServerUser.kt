package com.maricoolsapps.serverdatabase

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import javax.inject.Inject

class ServerUser
@Inject constructor(var auth: FirebaseAuth) {

    val signOut = auth.signOut()

     val currentUser: FirebaseUser? = auth.currentUser

    fun getUserId(): String?{
        return currentUser?.uid
    }

    fun registerUser(email: String, password: String): Task<AuthResult>{
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun signInUser(email: String, password: String): Task<AuthResult>{
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun updateProfileName(name: String): Task<Void>?{
        if (currentUser != null){
            val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            return currentUser.updateProfile(profile)
        }
        else{
            return null
        }
    }

    private fun getUserEmail(): String?{
        return currentUser?.email
    }

    fun getUserName(): String?{
        return currentUser?.displayName
    }

    fun getProfilePhoto(): Uri?{
        return currentUser?.photoUrl
    }

    fun changeProfilePhoto(uri: Uri): Task<Void>? {
        val profile = UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
       return currentUser?.updateProfile(profile)
    }

    fun reAuthenticate(oldPassword: String): Task<Void>? {
        val mail = getUserEmail()!!
        val credentials = EmailAuthProvider.getCredential(mail, oldPassword)
        return currentUser?.reauthenticate(credentials)
    }

    fun changeUsername(name: String): Task<Void>? {
        val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        if (currentUser != null) {
            return currentUser.updateProfile(profile)
        }else{
            return null
        }
    }

    fun changePasword(newPassword: String): Task<Void>? {
        if (currentUser != null) {
            return currentUser.updatePassword(newPassword)
        }
        else{
            return null
        }
    }
}