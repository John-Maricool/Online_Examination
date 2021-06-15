package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServerCloudData(var cloud: FirebaseFirestore,
                    var serverUser: ServerUser,
                    var scope: CoroutineScope) {

    companion object{
        val collectionName = "Admins"
    }

    fun addToFirebase(key: String, data: Any): LiveData<MyServerDataState>{
        val _data = MutableLiveData<MyServerDataState>()
        scope.launch {
            val result = cloud.collection(collectionName).document(serverUser.getUserId()!!).collection(key).add(data)
                    result.addOnSuccessListener {
                _data.postValue(MyServerDataState.onLoaded)
            }
                    .addOnFailureListener {
                        _data.postValue(MyServerDataState.notLoaded(it))
                    }
        }
        return _data
    }
}