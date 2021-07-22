package com.maricoolsapps.utils.cloud_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.toObjects
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.QuizSettingModel
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.constants.collectionName
import com.maricoolsapps.utils.others.constants.quizDocs
import com.maricoolsapps.utils.others.constants.quizTime
import com.maricoolsapps.utils.others.constants.registeredStudents
import com.maricoolsapps.utils.others.constants.settings
import com.maricoolsapps.utils.others.constants.studentsCollectionName
import com.maricoolsapps.utils.user.ServerUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminCloudData(var cloud: FirebaseFirestore,
                     var serverUser: ServerUser,
                     var scope: CoroutineScope) {

   suspend fun CreateFirestoreUser(user: AdminUser, Auth: FirebaseAuth): Boolean {
       return try {
           if (Auth.currentUser != null) {
               cloud.collection(collectionName)
                       .document(Auth.currentUser.uid).set(user).await()
               true
           } else {
               false
           }
       } catch (e: Exception) {
           false
        }
    }

    fun deleteAllQuizDocs(): LiveData<Boolean> {
        val _data = MutableLiveData<Boolean>()
        scope.launch {
            try{
            val shot = cloud.collection(collectionName).document(serverUser.getUserId())
                    .collection(quizDocs).get().await()
                        val docs = shot.documents
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
                    }  catch (e: Exception){
                        _data.postValue(false)
                    }
        }
        return _data
    }

    fun addToFirebase(data: Any, time: Int): LiveData<MyServerDataState> {
        val _data = MutableLiveData<MyServerDataState>()
        scope.launch {
            try{
                val setting = QuizSettingModel(time)
               val job =  cloud.collection(collectionName).document(serverUser.getUserId()).collection(settings).document(quizTime).set(setting)
               job.await()
                if (job.isComplete){
                    cloud.collection(collectionName).document(serverUser.getUserId()).collection(quizDocs).add(data).await()
                }
                _data.postValue(MyServerDataState.onLoaded)
            }catch (e: Exception) {
                        _data.postValue(MyServerDataState.notLoaded(e))
                    }
        }
        return _data
    }

    fun getRegisteredStudents(): LiveData<MyDataState> {
        val data = MutableLiveData<MyDataState>()
        scope.launch(IO) {
            try{
            val it = cloud.collection(collectionName)
                    .document(serverUser.getUserId()).collection(registeredStudents).get().await()
                        val ans = it.toObjects(StudentUser::class.java)
                        data.postValue(MyDataState.onLoaded(ans))
                    }catch (e: Exception) {
                        data.postValue(MyDataState.notLoaded(e))
                    }
        }
        return data
    }

    fun activateQuizForStudents(): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO){
            try{
            val users = cloud.collection(collectionName)
                    .document(serverUser.getUserId()).collection(registeredStudents).get(Source.SERVER).await()
                val student = users.toObjects<StudentUser>()
                val ids = mutableListOf<String>()
                student.forEach {
                    ids.add(it.id)
                }
                if (users != null) {
                    for (doc in users.documents){
                        doc.reference.update("activated", true)
                    }
                    for (i in 0 until ids.size){
                        val stu = cloud.collection(studentsCollectionName).document(ids[i]).get().await()
                        stu.reference.update("activated", true)
                    }
                }else{
                    data.postValue(MyServerDataState.notLoaded(java.lang.Exception("No students registered")))
                }
                data.postValue(MyServerDataState.onLoaded)
        }catch (e: java.lang.Exception){
                data.postValue(MyServerDataState.notLoaded(java.lang.Exception("Error Activating students")))
            }
        }
        return data
    }

    fun deactivateQuizForStudents(): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch(IO){
            try{
                val users = cloud.collection(collectionName)
                        .document(serverUser.getUserId()).collection(registeredStudents).get().await()
                val student = users.toObjects<StudentUser>()
                val ids = mutableListOf<String>()
                student.forEach {
                    ids.add(it.id)
                }
                if (users != null) {
                    for (doc in users.documents){
                        doc.reference.update("activated", false)
                    }
                    for (i in 0 until ids.size){
                        val stu = cloud.collection(studentsCollectionName).document(ids[i]).get().await()
                        stu.reference.update("activated", false)
                    }
                }else{
                    data.postValue(MyServerDataState.notLoaded(java.lang.Exception("No students registered")))
                }
                data.postValue(MyServerDataState.onLoaded)
            }catch (e: java.lang.Exception){
                data.postValue(MyServerDataState.notLoaded(java.lang.Exception("Error Activating students")))
            }
        }
        return data
    }

    suspend fun changeAdminName(name: String): Boolean{
        return try{
            cloud.collection(collectionName).document(serverUser.getUserId()).update("name", name).await()
            true
        }catch (e: java.lang.Exception){
            false
        }
    }

    suspend fun changeAdminEmail(mail: String): Boolean{
        return try{
            cloud.collection(collectionName).document(serverUser.getUserId()).update("email", mail).await()
            true
        }catch (e: java.lang.Exception){
            false
        }
    }

    suspend fun changeAdminPhotoUri(uri: String): Boolean{
        return try{
            cloud.collection(collectionName).document(serverUser.getUserId()).update("photoUri", uri).await()
            true
        }catch (e: java.lang.Exception){
            false
        }
    }

    fun deleteRegisteredStudent(ids: List<String>): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        scope.launch(IO) {
            try {
                ids.forEach { id ->
                    cloud.collection(collectionName)
                            .document(serverUser.getUserId()).collection(registeredStudents).document(id).delete().await()
                    cloud.collection(studentsCollectionName).document(id).update("registered", false).await()
                }
                data.postValue(true)
            } catch (E: Exception) {
                data.postValue(false)
            }
        }
        return data
    }
}