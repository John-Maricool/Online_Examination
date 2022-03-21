package com.maricoolsapps.utils.cloud_data

import com.google.firebase.firestore.DocumentSnapshot
import com.maricoolsapps.utils.source.FirestoreSource
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.models.QuizSettingModel
import com.maricoolsapps.utils.models.StudentUser
import java.lang.Exception

class StudentCloudData(var cloud: FirestoreSource) {

    suspend fun CreateFirestoreUser(user: StudentUser, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            cloud.setFirestoreStudent(user)
            b.invoke(MyDataState.success("Successfull"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    /*
    suspend fun changeStudentName(name: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("name", name).await()
            true
        }catch (e: Exception){
            false
        }
    }

    suspend fun changeStudentEmail(mail: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("email", mail).await()
            true
        }catch (e: Exception){
            false
        }
    }

    suspend fun changeStudentPhotoUri(uri: String): Boolean{
        return try{
            val imageUri = uri.toUri()
            val ref = FirebaseStorage.getInstance().getReference("${serverUser.getUserId()}.jpg").putFile(imageUri)
            ref.await()
            if(ref.isComplete){
                val imgUrl = FirebaseStorage.getInstance().getReference("${serverUser.getUserId()}.jpg").downloadUrl.await()
                Log.d("url", imgUrl.toString())
                cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("photoUri", imgUrl.toString()).await()
            }
            true
        }catch (e: Exception){
            false
        }
    }

    suspend fun changeStudentNumber(number: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("number", number).await()
            true
        }catch (e: Exception){
            false
        }
    }

    suspend fun changeStudentRegNo(regno: String): Boolean{
        return try{
            cloud.collection(studentsCollectionName).document(serverUser.getUserId()).update("regNo", regno).await()
            true
        }catch (e: Exception){
            false
        }
    }*/

    suspend fun registerForQuiz(
        adminId: String,
        userId: String,
        b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            val res = cloud.checkIfAdminDocExist(adminId)
            if (!res) {
                cloud.registerStudentForQuiz(adminId, userId)
                cloud.addStudentToAdminList(adminId, userId)
                b.invoke(MyDataState.success("Successful"))
            } else {
                b.invoke(MyDataState.error("You're already registered", null))
            }
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun checkIfPreviouslyRegistered(
        userId: String, b: (MyDataState<Boolean>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            val data = cloud.getStudent(userId)?.toObject(StudentUser::class.java)
            if (data!!.isRegistered) {
                b.invoke(MyDataState.success(true))
            } else {
                b.invoke(MyDataState.success(false))
            }
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }


    suspend fun checkIfItsTimeToAccessQuiz(
        userId: String, b: (MyDataState<Boolean>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            val data = cloud.getStudent(userId)?.toObject(StudentUser::class.java)
            if (data!!.isActivated) {
                b.invoke(MyDataState.success(true))
            } else {
                b.invoke(MyDataState.success(false))
            }
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun deactivateStudent(studentId: String, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            cloud.deactivateStudent(studentId)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun sendQuizResult(score: Int, userId: String, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            cloud.updateQuizResult(score, userId)
            cloud.deactivateStudent(userId)
            b.invoke(MyDataState.success("successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun getStudent(userId: String, b: (MyDataState<StudentUser>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            val user_obj = cloud.getStudent(userId)?.toObject(StudentUser::class.java)
            b.invoke(MyDataState.success(user_obj))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun changeProfilePhoto(
        userId: String,
        photo: String,
        b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            cloud.changeStudentProfilePhoto(userId, photo)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun changeProfileName(
        userId: String,
        name: String,
        b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            cloud.changeStudentProfileName(userId, name)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun changeProfileEmail(
        userId: String,
        email: String,
        b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            cloud.changeStudentProfileEmail(userId, email)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun getAllQuizDocId(studentId: String, b: (MyDataState<List<String>>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            val quizzes = mutableListOf<String>()
            cloud.getAllQuizDocId(studentId).forEach {
                quizzes.add(it.reference.id)
            }
            b.invoke(MyDataState.success(quizzes))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun getQuiz(
        userId: String,
        docId: String,
        b: (MyDataState<DocumentSnapshot>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            val result = cloud.getQuizQuestion(userId, docId)
            b.invoke(MyDataState.success(result))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun getQuizTime(
        userId: String,
        b: (MyDataState<QuizSettingModel>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            val result = cloud.getQuizTime(userId)?.toObject(QuizSettingModel::class.java)
            b.invoke(MyDataState.success(result))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }
}