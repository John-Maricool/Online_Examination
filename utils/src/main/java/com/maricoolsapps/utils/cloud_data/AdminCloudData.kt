package com.maricoolsapps.utils.cloud_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.utils.ServerUser
import com.maricoolsapps.utils.constants.collectionName
import com.maricoolsapps.utils.constants.details
import com.maricoolsapps.utils.constants.documentUserName
import com.maricoolsapps.utils.constants.quizDocs
import com.maricoolsapps.utils.constants.registeredStudents
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.StudentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class AdminCloudData(var cloud: FirebaseFirestore,
                     var serverUser: ServerUser,
                     var scope: CoroutineScope) {

    val userDoc =  cloud.collection(collectionName)
            .document(serverUser.getUserId()!!)


    fun CreateFirestoreUser(user: AdminUser, auth: FirebaseAuth)
            : LiveData<MyServerDataState> {
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            if (auth.currentUser != null){
                cloud.collection(collectionName)
                        .document(auth.currentUser.uid).set(user)
                        .addOnSuccessListener {
                    data.postValue(MyServerDataState.onLoaded)
                }.addOnFailureListener {
                    data.postValue(MyServerDataState.notLoaded(it))
                }
            }else{
                    data.postValue(MyServerDataState.notLoaded(Exception("Error")))
                }
            }
        return data
    }

    fun deleteAllQuizDocs(): LiveData<Boolean>{
        val _data = MutableLiveData<Boolean>()
        scope.launch {
            userDoc.collection(quizDocs).get()
                    .addOnSuccessListener {
                        val docs = it.documents
                        if (docs.size > 0) {
                            docs.forEach { snapshot ->
                                snapshot.reference.delete().addOnSuccessListener {
                                    _data.postValue(true)
                                }.addOnFailureListener {
                                    _data.postValue(false)
                                }
                            }
                        }else{
                            _data.postValue(null)
                        }
                    }.addOnFailureListener {
                        _data.postValue(false)
                    }
        }
        return _data
    }

    fun addToFirebase(data: Any): LiveData<MyServerDataState> {
        val _data = MutableLiveData<MyServerDataState>()
        scope.launch {
            val result = userDoc.collection(quizDocs).add(data)
            result.addOnSuccessListener {
                _data.postValue(MyServerDataState.onLoaded)
            }
                    .addOnFailureListener {
                        _data.postValue(MyServerDataState.notLoaded(it))
                    }
        }
        return _data
    }

    fun getList(): LiveData<MyDataState> {
        val data = MutableLiveData<MyDataState>()
        scope.launch(IO) {
            userDoc.collection(registeredStudents).get()
                    .addOnSuccessListener {
                        val ans = it.toObjects(StudentUser::class.java)
                        data.postValue(MyDataState.onLoaded(ans))
                    }.addOnFailureListener { e ->
                        data.postValue(MyDataState.notLoaded(e))
                    }
        }
        return data
    }
}