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

    companion object{
        var profileUri: Uri? = null
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

    fun changeProfilePhoto(uri: Uri): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            try{
            val profile = UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
            auth.currentUser?.updateProfile(profile)?.await()
                profileUri = uri
                data.postValue(MyServerDataState.onLoaded)
            }catch (e: Exception) {
                data.postValue(MyServerDataState.notLoaded(e))
            }
        }
        return data
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

    fun changeUsername(name: String): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            try{
            val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            auth.currentUser?.updateProfile(profile)?.await()
                data.postValue(MyServerDataState.onLoaded)
            }catch (e: Exception) {
                data.postValue(MyServerDataState.notLoaded(e))
            }
        }
        return data
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