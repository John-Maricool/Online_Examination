package com.maricoolsapps.utils.cloud_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.maricoolsapps.utils.ServerUser
import com.maricoolsapps.utils.constants.collectionName
import com.maricoolsapps.utils.constants.studentsCollectionName
import com.maricoolsapps.utils.constants.studentsDocumentUserName
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.StudentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class StudentCloudData(var cloud: FirebaseFirestore,
                       var serverUser: ServerUser,
                       var scope: CoroutineScope) {

    val userDataDoc = cloud.collection(studentsCollectionName)
            .document(serverUser.getUserId()!!)

    fun CreateFirestoreUser(user: StudentUser)
            : LiveData<MyServerDataState> {
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            userDataDoc.set(user).addOnSuccessListener {
                data.postValue(MyServerDataState.onLoaded)
            }.addOnFailureListener {
                data.postValue(MyServerDataState.notLoaded(it))
            }
        }
        return data
    }

    fun registerForQuiz(id: String): LiveData<MyServerDataState> {
        val dataLiveData = MutableLiveData<MyServerDataState>()
        userDataDoc.update("registered", true)
                userDataDoc.get().addOnCompleteListener { snapshot ->
            if (snapshot.isSuccessful){
                cloud.collection(collectionName).document(id)
                        .collection("Registered Students")
                        .add(snapshot.result?.toObject(StudentUser::class.java)!!)
                dataLiveData.postValue(MyServerDataState.onLoaded)
            }
                    else{
                dataLiveData.postValue(MyServerDataState.notLoaded(snapshot.exception!!))
            }
        }
        return dataLiveData
    }

    fun checkIfPreviouslyRegistered(): LiveData<Boolean> {
    val dataLiveData = MutableLiveData<Boolean>()
    scope.launch(IO) {
        userDataDoc.get().addOnCompleteListener { snapshot ->
            if (snapshot.isSuccessful) {
                val data = snapshot.result?.toObject(StudentUser::class.java)
                    if (data!!.isRegistered) {
                        dataLiveData.postValue(true)
                    } else {
                        dataLiveData.postValue(false)
                    }
            } else {
                dataLiveData.postValue(null)
            }
        }
    }
    return dataLiveData
}

    fun checkIfAdminDocExist(id: String): LiveData<Boolean> {
        val dataLiveData = MutableLiveData<Boolean>()
        scope.launch(IO) {
             cloud.collection(collectionName).document(id).get(Source.SERVER)
                    .addOnSuccessListener {res ->
                    if (res.exists()) {
                        dataLiveData.postValue(true)
                    } else {
                        dataLiveData.postValue(false)
                    }
                }
                    .addOnFailureListener {
                    dataLiveData.postValue(null)
                }
            }
        return dataLiveData
    }
}