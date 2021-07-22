package com.maricoolsapps.utils.cloud_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.google.protobuf.ListValue
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.user.ServerUser
import com.maricoolsapps.utils.others.constants.collectionName
import com.maricoolsapps.utils.others.constants.studentsCollectionName
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.QuizSettingModel
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.constants.quizDocs
import com.maricoolsapps.utils.others.constants.quizTime
import com.maricoolsapps.utils.others.constants.registeredStudents
import com.maricoolsapps.utils.others.constants.settings
import com.maricoolsapps.utils.others.constants.time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class StudentCloudData(var cloud: FirebaseFirestore,
                       var serverUser: ServerUser,
                       var scope: CoroutineScope) {

    suspend fun CreateFirestoreUser(user: StudentUser, Auth: FirebaseAuth): Boolean {
        return try {
            if (Auth.currentUser != null) {
                cloud.collection(studentsCollectionName)
                        .document(Auth.currentUser.uid).set(user).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun changeStudentName(name: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("name", name).await()
            true
        }catch (e: java.lang.Exception){
            false
        }
    }

    suspend fun changeStudentEmail(mail: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("email", mail).await()
            true
        }catch (e: java.lang.Exception){
            false
        }
    }

    suspend fun changeStudentPhotoUri(uri: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("photoUri", uri).await()
            true
        }catch (e: java.lang.Exception){
            false
        }
    }

    suspend fun changeStudentNumber(number: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("number", number).await()
            true
        }catch (e: java.lang.Exception){
            false
        }
    }

    suspend fun changeStudentRegNo(regno: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("regNo", regno).await()
            true
        }catch (e: java.lang.Exception){
            false
        }
    }

    fun registerForQuiz(id: String): LiveData<MyServerDataState> {
        val dataLiveData = MutableLiveData<MyServerDataState>()
        scope.launch(IO) {
          val job = async(IO) {
                checkIfAdminDocExist(id)
            }
            job.await().let {
                when(it){
                    true -> {
                        try{
                            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("adminId", id)
                            cloud.collection(studentsCollectionName)
                                    .document(serverUser.getUserId()).update("registered", true).await()
                            val snapshot = cloud.collection(studentsCollectionName)
                                    .document(serverUser.getUserId()).get().await()
                            cloud.collection(collectionName).document(id)
                                    .collection(registeredStudents).document(serverUser.getUserId())
                                    .set(snapshot.toObject<StudentUser>()!!).await()
                            dataLiveData.postValue(MyServerDataState.onLoaded)
                        }
                        catch (e: Exception){
                            dataLiveData.postValue(MyServerDataState.notLoaded(Exception("Check Internet")))
                        }
                    }
                    false -> {
                        dataLiveData.postValue(MyServerDataState.notLoaded(Exception("Wrong ID")))
                    }
                }
            }
        }
        return dataLiveData
    }

    fun checkIfPreviouslyRegistered(): LiveData<Boolean> {
    val dataLiveData = MutableLiveData<Boolean>()
    scope.launch(IO) {
        try{
        val snapshot = cloud.collection(studentsCollectionName)
                .document(serverUser.getUserId()).get().await()
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

    private suspend fun checkIfAdminDocExist(id: String): Boolean {
        return try {
            val res = cloud.collection(collectionName).document(id).get(Source.SERVER).await()
            res.exists()
        } catch (e: Exception) {
            false
        }
    }

    fun checkIfItsTimeToAccessQuiz(): LiveData<Boolean>{
        val bool = MutableLiveData<Boolean>()
        scope.launch {
             try {
                val snapshot = cloud.collection(studentsCollectionName).document(serverUser.getUserId()).get().await()
                val user = snapshot.toObject<StudentUser>()
                if (user!!.isActivated) {
                   bool.postValue(true)
                } else {
                    bool.postValue(false)
                }
            } catch (e: Exception) {
                bool.postValue(null)
            }
        }
        return bool
    }

     suspend fun downloadQuiz(): MyDataState {
         return try {
             val id = cloud.collection(studentsCollectionName).document(serverUser.getUserId()).get().await()
             val ids = id.toObject<StudentUser>()?.adminId
             val job =  cloud.collection(collectionName).document(ids!!).collection(settings).document(quizTime).get(Source.SERVER).await()
                 time = job.toObject<QuizSettingModel>()?.time
             val ans = cloud.collection(collectionName).document(ids).collection(quizDocs).get(Source.SERVER).await()
             MyDataState.onLoaded(ans)
         }catch (e: Exception){
             MyDataState.notLoaded(e)
         }
    }

    fun deactivateStudent(): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        scope.launch {
            try{
                cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("activated", false).await()
                val user = cloud.collection(studentsCollectionName).document(serverUser.getUserId()).get().await()
                val id = user.toObject<StudentUser>()?.adminId
                cloud.collection(collectionName).document(id!!).collection(registeredStudents)
                        .document(serverUser.getUserId()).update("activated", false).await()
                data.postValue(MyServerDataState.onLoaded)
            }catch (e: Exception){
                data.postValue(MyServerDataState.notLoaded(e))
            }
        }
        return data
    }

    fun sendQuizResult(score: Int): LiveData<MyServerDataState>{
        val data = MutableLiveData<MyServerDataState>()
        val userDoc =  cloud.collection(studentsCollectionName).document(serverUser.getUserId())
        scope.launch {
            try{
               userDoc.update("quizScore", score).await()
                       val user = userDoc.get().await()
                val id = user.toObject<StudentUser>()?.adminId
                cloud.collection(collectionName).document(id!!).collection(registeredStudents)
                        .document(serverUser.getUserId()).update("quizScore", score).await()
                data.postValue(MyServerDataState.onLoaded)
            }catch (e: Exception){
                data.postValue(MyServerDataState.notLoaded(e))
            }
        }
        return data
    }

    fun getStudent(): LiveData<MyDataState>{
        val data = MutableLiveData<MyDataState>()
        val userDoc =  cloud.collection(studentsCollectionName).document(serverUser.getUserId())
        scope.launch {
            try{
                val user = userDoc.get().await()
                val user_obj = user.toObject<StudentUser>()
                data.postValue(MyDataState.onLoaded(user_obj as StudentUser))
            }catch (e: Exception){
                data.postValue(MyDataState.notLoaded(e))
            }
        }
        return data
    }
}