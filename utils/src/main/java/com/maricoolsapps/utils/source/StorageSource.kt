package com.maricoolsapps.utils.source

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageSource
@Inject constructor(val storage: FirebaseStorage){

    suspend fun getDownloadUri(id: String): Uri? {
        return storage.getReference("${id}.jpg").downloadUrl.await()
    }

    suspend fun putFileInStorage(uri: Uri, id: String): UploadTask.TaskSnapshot? {
        return storage.getReference("${id}.jpg").putFile(uri).await()
    }

}