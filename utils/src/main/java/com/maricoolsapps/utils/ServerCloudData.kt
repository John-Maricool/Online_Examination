package com.maricoolsapps.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.RegisteredUsersModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ServerCloudData(var cloud: FirebaseFirestore,
                      var serverUser: ServerUser,
                      var scope: CoroutineScope) {

    companion object {
        const val collectionName = "Admins"
        const val documentUserName = "User Details"
        const val quizDocs = "Quiz"
        const val registeredStudents = "Registered Students"

    }

    fun addToFirebase(data: Any): LiveData<MyServerDataState> {
        val _data = MutableLiveData<MyServerDataState>()
        scope.launch {
            val result = cloud.collection(collectionName).document(serverUser.getUserId()!!).collection(quizDocs).add(data)
            result.addOnSuccessListener {
                _data.postValue(MyServerDataState.onLoaded)
            }
                    .addOnFailureListener {
                        _data.postValue(MyServerDataState.notLoaded(it))
                    }
        }
        return _data
    }

    fun CreateFirestoreUser(name: String, email: String): LiveData<MyServerDataState> {
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            val user = AdminUser(name, email, null)
            cloud.collection(collectionName).document(documentUserName).set(user).addOnSuccessListener {
                data.postValue(MyServerDataState.onLoaded)
            }.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }

    fun updateProfileUri(uri: String): LiveData<MyServerDataState> {
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            val ref = cloud.collection(collectionName).document(documentUserName)
            ref.update("photoUri", uri).addOnSuccessListener {
                data.postValue(MyServerDataState.onLoaded)
            }.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }

     fun getList(): LiveData<MyDataState> {
        val data = MutableLiveData<MyDataState>()
        scope.launch(IO) {
            cloud.collection(collectionName).document(serverUser.getUserId()!!).collection(registeredStudents).get()
                    .addOnSuccessListener {
                        val ans = it.toObjects(RegisteredUsersModel::class.java)
                        data.postValue(MyDataState.onLoaded(ans))
                    }.addOnFailureListener { e ->
                        data.postValue(MyDataState.notLoaded(e))
                    }
        }
        return data
    }

    fun updateProfileName(name: String): LiveData<MyServerDataState> {
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            val ref = cloud.collection(collectionName).document(documentUserName)
            ref.update("name", name).addOnSuccessListener {
                data.postValue(MyServerDataState.onLoaded)
            }.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }

    fun getProfileUri(): LiveData<String?>{
        val data = MutableLiveData<String?>()
        scope.launch(IO) {
            val ref = cloud.collection(collectionName).document(documentUserName)
            ref.get().addOnSuccessListener {
                val result = it.toObject(AdminUser::class.java)
                val uri = result?.photoUri
                data.postValue(uri)
            }.addOnFailureListener {
                data.postValue(null)
            }
        }
        return data
    }
}