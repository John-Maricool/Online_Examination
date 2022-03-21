package com.maricoolsapps.utils.source

import com.google.firebase.auth.*
import com.maricoolsapps.utils.models.SignInDetails
import kotlinx.coroutines.tasks.await

class ServerUser
constructor(var auth: FirebaseAuth) {

    fun getUserId(): String {
        return auth.currentUser?.uid!!
    }

    suspend fun registerUser(details: SignInDetails): AuthResult? {
        return auth.createUserWithEmailAndPassword(details.email, details.password).await()
    }

    suspend fun signInUser(details: SignInDetails): AuthResult? {
        return auth.signInWithEmailAndPassword(details.email, details.password).await()
    }

    suspend fun changeEmail(mail: String): Void? {
        return auth.currentUser?.updateEmail(mail)?.await()
    }

    suspend fun changePassword(newPassword: String): Void? {
        return auth.currentUser?.updatePassword(newPassword)?.await()
    }

    suspend fun changeEmail(email: String, user: SignInDetails): FirebaseUser? {
        val provider = EmailAuthProvider.getCredential(user.email, user.password)
        return auth.currentUser?.apply {
            reauthenticate(provider).await()
            updateEmail(email).await()
        }
    }
}