package com.maricoolsapps.utils.user

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.maricoolsapps.utils.datastate.MyServerDataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ServerUser
  constructor(var auth: FirebaseAuth, var scope: CoroutineScope) {

    fun getUserId(): String{
        return auth.currentUser?.uid!!
    }

    suspend fun registerUser(email: String, password: String, name: String): Boolean{
        return try{
            auth.createUserWithEmailAndPassword(email, password).await()
            updateProfileName(name)
            true
        }catch (e: Exception) {
            false
        }
        }

    fun signInUser(email: String, password: String): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            try{
            auth.signInWithEmailAndPassword(email, password).await()
                data.postValue(MyServerDataState.onLoaded)
            }catch (e: Exception){
                data.postValue(MyServerDataState.notLoaded(e))
            }
        }
        return data
    }

    suspend fun updateProfileName(name: String): Boolean{
        return try{
            val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            auth.currentUser?.updateProfile(profile)?.await()
            true
        }catch (e: Exception) {
            false
        }
    }

     fun getUserEmail(): String?{
        return auth.currentUser?.email
    }

    fun getUserName(): String?{
        return auth.currentUser?.displayName
    }

    fun getProfilePhoto(): LiveData<Uri?>{
        val data = MutableLiveData<Uri?>()
        scope.launch(IO) {
            data.postValue(auth.currentUser?.photoUrl)
        }
        return data
    }

    suspend fun changeProfilePhoto(uri: Uri): Boolean{
        return try{
            val profile = UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build()
            auth.currentUser?.updateProfile(profile)?.await()
            true
        }catch (e: Exception) {
            false
        }
        }

    fun reAuthenticate(oldPassword: String): LiveData<MyServerDataState>{
            val data = MutableLiveData<MyServerDataState>()
            scope.launch(IO) {
                try{
                val mail = getUserEmail()!!
                val credentials = EmailAuthProvider.getCredential(mail, oldPassword)
                 auth.currentUser?.reauthenticate(credentials)?.await()
                    data.postValue(MyServerDataState.onLoaded)
                }catch (e: Exception){
                    data.postValue(MyServerDataState.notLoaded(e))
                }
            }
            return data
        }

    suspend fun changeUsername(name: String): Boolean{
        return try{
            val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            auth.currentUser?.updateProfile(profile)?.await()
            true
        }catch (e: Exception) {
            false
        }
        }

    suspend fun changeEmail(mail: String): Boolean{
        return try{
            auth.currentUser?.updateEmail(mail)?.await()
            true
        }catch (e: Exception) {
            false
        }
        }

    fun changePassword(newPassword: String): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            try{
                auth.currentUser?.updatePassword(newPassword)?.await()
                data.postValue(MyServerDataState.onLoaded)
            }catch (e: Exception){
                data.postValue(MyServerDataState.notLoaded(e))
            }
        }
        return data
    }
}