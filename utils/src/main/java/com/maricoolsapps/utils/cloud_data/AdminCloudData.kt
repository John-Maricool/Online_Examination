package com.maricoolsapps.utils.cloud_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.utils.user.ServerUser
import com.maricoolsapps.utils.others.constants.collectionName
import com.maricoolsapps.utils.others.constants.quizDocs
import com.maricoolsapps.utils.others.constants.registeredStudents
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.constants.studentsCollectionName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception

class AdminCloudData(var cloud: FirebaseFirestore,
                     var serverUser: ServerUser,
                     var scope: CoroutineScope) {

    fun CreateFirestoreUser(user: AdminUser, auth: FirebaseAuth)
            : LiveData<MyServerDataState> {
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            auth.addAuthStateListener { auth ->
                if (auth.currentUser != null) {
                    cloud.collection(collectionName)
                            .document(auth.currentUser.uid).set(user)
                            .addOnSuccessListener {
                                data.postValue(MyServerDataState.onLoaded)
                            }.addOnFailureListener {
                                data.postValue(MyServerDataState.notLoaded(it))
                            }
                } else {
                    data.postValue(MyServerDataState.notLoaded(Exception("Error")))
                }
            }
        }
        return data
    }

    fun deleteAllQuizDocs(): LiveData<Boolean> {
        val _data = MutableLiveData<Boolean>()
        scope.launch {
            cloud.collection(collectionName)
                    .document(serverUser.getUserId()).collection(quizDocs).get()
                    .addOnSuccessListener {
                        val docs = it.documents
                        if (docs.isNotEmpty()) {
                            try {
                                for (doc in docs) {
                                    doc.reference.delete()
                                }
                                _data.postValue(true)
                            } catch (e: Exception) {
                                _data.postValue(false)
                            }
                        } else {
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
            val result = cloud.collection(collectionName)
                    .document(serverUser.getUserId()).collection(quizDocs).add(data)
            result.addOnSuccessListener {
                _data.postValue(MyServerDataState.onLoaded)
            }
                    .addOnFailureListener {
                        _data.postValue(MyServerDataState.notLoaded(it))
                    }
        }
        return _data
    }

    fun getRegisteredStudents(): LiveData<MyDataState> {
        val data = MutableLiveData<MyDataState>()
        scope.launch(IO) {
            cloud.collection(collectionName)
                    .document(serverUser.getUserId()).collection(registeredStudents).get()
                    .addOnSuccessListener {
                        val ans = it.toObjects(StudentUser::class.java)
                        data.postValue(MyDataState.onLoaded(ans))
                    }.addOnFailureListener { e ->
                        data.postValue(MyDataState.notLoaded(e))
                    }
        }
        return data
    }

    fun deleteRegisteredStudent(ids: List<String>): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        scope.launch(IO) {
            try {
                ids.forEach { id ->
                    cloud.collection(collectionName)
                            .document(serverUser.getUserId()).collection(registeredStudents).document(id).delete()
                    cloud.collection(studentsCollectionName).document(id).update("registered", false)
                }
                data.postValue(true)
            } catch (E: Exception) {
                data.postValue(false)
            }
        }
        return data
    }

}