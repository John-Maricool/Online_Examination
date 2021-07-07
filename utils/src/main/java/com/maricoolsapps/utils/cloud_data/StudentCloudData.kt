package com.maricoolsapps.utils.cloud_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.user.ServerUser
import com.maricoolsapps.utils.others.constants.collectionName
import com.maricoolsapps.utils.others.constants.studentsCollectionName
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.QuizSettingModel
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.constants
import com.maricoolsapps.utils.others.constants.admin_id
import com.maricoolsapps.utils.others.constants.quizDocs
import com.maricoolsapps.utils.others.constants.registeredStudents
import com.maricoolsapps.utils.others.constants.settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

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
        scope.launch(IO) {
          val job = async(IO) {
                checkIfAdminDocExist(id)
            }
            job.await().let {
                when(it){
                    true -> {
                        try{
                            userDataDoc.update("registered", true).await()
                            val snapshot = userDataDoc.get().await()
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

    private suspend fun checkIfAdminDocExist(id: String): Boolean {
        return try {
            val res = cloud.collection(collectionName).document(id).get(Source.SERVER).await()
            res.exists()
        } catch (e: Exception) {
            false
        }
    }

    fun checkIfItsTimeToAccessQuiz(): LiveData<Boolean>{
        val data = MutableLiveData<Boolean>()
        val date = Calendar.getInstance().time.time
        scope.launch {
            try {
               val snapshot =  cloud.collection(collectionName).document(constants.admin_id).collection(quizDocs).document(settings).get().await()
                val model = snapshot.toObject<QuizSettingModel>()
                val cloud_date = model?.stamp?.toDate()?.time
                if (date < cloud_date!!){
                    data.postValue(null)
                }else{
                    data.postValue(true)
                }
            }catch (e: Exception){
                data.postValue(false)
            }
        }
        return data
    }

     fun downloadQuiz(): LiveData<MyDataState> {
        val data = MutableLiveData<MyDataState>()
        scope.launch {
            try {
                val ans = cloud.collection(collectionName).document("EH4tf4KhiFWU1rZuLPsTMuUYCbl2").collection(quizDocs).get().await()
                data.postValue(MyDataState.onLoaded(ans))
            }catch (e: Exception){
                data.postValue(MyDataState.notLoaded(e))
            }
        }
         return data
    }
}