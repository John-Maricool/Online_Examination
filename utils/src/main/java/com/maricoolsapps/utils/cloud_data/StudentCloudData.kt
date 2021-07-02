package com.maricoolsapps.utils.cloud_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.maricoolsapps.utils.user.ServerUser
import com.maricoolsapps.utils.others.constants.collectionName
import com.maricoolsapps.utils.others.constants.studentsCollectionName
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.StudentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class StudentCloudData(var cloud: FirebaseFirestore,
                       var serverUser: ServerUser,
                       var scope: CoroutineScope) {

    val userDataDoc = cloud.collection(studentsCollectionName)
            .document(serverUser.getUserId())

    fun CreateFirestoreUser(user: StudentUser)
            : LiveData<MyServerDataState> {
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
            try{
            userDataDoc.set(user).await()
                data.postValue(MyServerDataState.onLoaded)
            }catch (e: Exception) {
                data.postValue(MyServerDataState.notLoaded(e))
            }
        }
        return data
    }

    fun registerForQuiz(id: String): LiveData<MyServerDataState> {
        val dataLiveData = MutableLiveData<MyServerDataState>()
        scope.launch {
            try{
        userDataDoc.update("registered", true).await()
                val snapshot = userDataDoc.get().await()
                cloud.collection(collectionName).document(id)
                        .collection("Registered Students").document(serverUser.getUserId())
                        .set(snapshot.toObject<StudentUser>()!!).await()
                 dataLiveData.postValue(MyServerDataState.onLoaded)
            }
                    catch (e: Exception){
                dataLiveData.postValue(MyServerDataState.notLoaded(Exception("Error")))
            }
        }
        return dataLiveData
    }

    fun checkIfPreviouslyRegistered(): LiveData<Boolean> {
    val dataLiveData = MutableLiveData<Boolean>()
    scope.launch(IO) {
        try{
        val snapshot = userDataDoc.get().await()
                val data = snapshot.toObject<StudentUser>()
            if (data!!.isRegistered) {
                        dataLiveData.postValue(true)
                    } else {
                        dataLiveData.postValue(false)
                    }
            } catch (e: Exception) {
                dataLiveData.postValue(null)
            }
        }
    return dataLiveData
}

    fun checkIfAdminDocExist(id: String): LiveData<Boolean> {
        val dataLiveData = MutableLiveData<Boolean>()
        scope.launch(IO) {
            try {
                val res = cloud.collection(collectionName).document(id).get(Source.SERVER).await()
                if (res.exists()) {
                    dataLiveData.postValue(true)
                } else {
                    dataLiveData.postValue(false)
                }
            } catch (e: Exception) {
                dataLiveData.postValue(null)
            }
        }
        return dataLiveData
    }

}