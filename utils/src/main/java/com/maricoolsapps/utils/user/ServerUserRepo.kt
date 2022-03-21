package com.maricoolsapps.utils.user

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.source.ServerUser
import com.maricoolsapps.utils.models.SignInDetails
import java.lang.Exception
import javax.inject.Inject

class ServerUserRepo
 @Inject constructor(var user: ServerUser) {

    fun getUserUid() = user.getUserId()

    suspend fun signInUser(details: SignInDetails, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            user.signInUser(details)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    fun getAuthState(): MutableLiveData<FirebaseUser?> {
        val result = MutableLiveData<FirebaseUser?>()
        user.auth.addAuthStateListener {
            if (it.currentUser == null && it.uid == null) {
                result.postValue(null)
            } else {
                result.postValue(it.currentUser)
            }
        }
        return result
    }

    suspend fun registerUser(details: SignInDetails, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            user.registerUser(details)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun changeEmail(mail: String, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            user.changeEmail(mail)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun changePassword(newPassword: String, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            user.changePassword(newPassword)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun changeEmail(email: String, details: SignInDetails): FirebaseUser? {
        return user.changeEmail(email, details)
    }
}