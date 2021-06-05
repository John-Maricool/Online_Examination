package com.maricoolsapps.serverdatabase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ServerCloudData
@Inject constructor(cloud: FirebaseFirestore,
                    serverUser: ServerUser) {

     var docRef: DocumentReference

    init {
        docRef = cloud.collection(collectionName).document(serverUser.getUserId()!!)
    }

    companion object{
        val collectionName = "Admins"
    }
}