package com.maricoolsapps.utils

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.*
import com.maricoolsapps.utils.datastate.MyServerDataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ServerUser
  constructor(var auth: FirebaseAuth, var scope: CoroutineScope) {
     val currentUser: FirebaseUser? = auth.currentUser

    fun getUserId(): String?{
        return currentUser?.uid
    }

    companion object{
        var profileUri: Uri? = null
    }

    fun registerUser(email: String, password: String, name: String): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                updateProfileName(name)
                data.postValue(MyServerDataState.onLoaded)
            }.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
       return data
    }

    fun signInUser(email: String, password: String): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                data.postValue(MyServerDataState.onLoaded)
            }.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }

    fun updateProfileName(name: String): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            currentUser?.updateProfile(profile)?.addOnSuccessListener {
                data.postValue(MyServerDataState.onLoaded)
            }?.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }

     fun getUserEmail(): String?{
        return currentUser?.email
    }

    fun getUserName(): String?{
        return currentUser?.displayName
    }

    fun getProfilePhoto(): LiveData<Uri?>{
        val data = MutableLiveData<Uri?>()
        scope.launch(IO) {
            data.postValue(currentUser?.photoUrl)
        }
        return data
    }

    fun changeProfilePhoto(uri: Uri): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            val profile = UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
            currentUser?.updateProfile(profile)?.addOnSuccessListener {
                profileUri = uri
                data.postValue(MyServerDataState.onLoaded)
            }?.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }

    fun reAuthenticate(oldPassword: String): LiveData<MyServerDataState>{
            val data = MutableLiveData<MyServerDataState>()
            scope.launch(IO) {
                val mail = getUserEmail()!!
                val credentials = EmailAuthProvider.getCredential(mail, oldPassword)
                 currentUser?.reauthenticate(credentials)?.addOnSuccessListener {
                    data.postValue(MyServerDataState.onLoaded)
                }?.addOnFailureListener {
                    data.postValue(MyServerDataState.notLoaded(it))
                }
            }
            return data
        }

    fun changeUsername(name: String): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            val profile = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            currentUser?.updateProfile(profile)?.addOnSuccessListener {
                data.postValue(MyServerDataState.onLoaded)
            }?.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }

    fun changePassword(newPassword: String): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            currentUser?.updatePassword(newPassword)?.addOnSuccessListener {
                data.postValue(MyServerDataState.onLoaded)
            }?.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }
}