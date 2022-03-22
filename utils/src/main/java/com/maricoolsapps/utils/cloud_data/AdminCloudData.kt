package com.maricoolsapps.utils.cloud_data

import androidx.core.net.toUri
import com.maricoolsapps.utils.source.FirestoreSource
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.QuizSettingModel
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.source.StorageSource

class AdminCloudData(
    var cloud: FirestoreSource,
    val storage: StorageSource
) {

    suspend fun CreateFirestoreUser(user: AdminUser, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            cloud.setFirestoreAdmin(user)
            b.invoke(MyDataState.success("Successfully Loaded"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun addOverriteToDb(
        userId: String, data: List<Any>,
        time: QuizSettingModel, b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            cloud.getAllQuizDocs(userId)?.forEach {
                it.reference.delete()
            }
            cloud.addQuizQuestionsToDb(userId, data)
            cloud.addQuizTime(userId, time)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: java.lang.Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }


/*

    suspend fun changeAdminName(name: String): Boolean {
        return try {
            cloud.collection(collectionName).document(serverUser.getUserId()).update("name", name)
                .await()
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }

    suspend fun changeAdminEmail(mail: String): Boolean {
        return try {
            cloud.collection(collectionName).document(serverUser.getUserId()).update("email", mail)
                .await()
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }

    suspend fun changeAdminPhotoUri(uri: String): Boolean {
        return try {
            val imageUri = uri.toUri()
            val ref = FirebaseStorage.getInstance().getReference("${serverUser.getUserId()}.jpg")
                .putFile(imageUri)
            ref.await()
            if (ref.isComplete) {
                val imgUrl = FirebaseStorage.getInstance()
                    .getReference("${serverUser.getUserId()}.jpg").downloadUrl.await()
                cloud.collection(collectionName).document(serverUser.getUserId())
                    .update("photoUri", imgUrl.toString()).await()
            }
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }
*/

    suspend fun addToFirebase(
        userId: String,
        data: List<Any>,
        time: QuizSettingModel,
        b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            cloud.addQuizQuestionsToDb(userId, data)
            cloud.addQuizTime(userId, time)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun getRegisteredStudents(
        userId: String,
        b: (MyDataState<List<StudentUser>>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        val students = mutableListOf<StudentUser>()
        try {
            val result = cloud.getRegisteredStudents(userId)?.documents
            result?.forEach {
                val student = cloud.getStudent(it.reference.id)?.toObject(StudentUser::class.java)
                if (student != null) {
                    students.add(student)
                }
            }
            b.invoke(MyDataState.success(students))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun activateQuizForStudents(
        userId: String, b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            val result = cloud.getRegisteredStudents(userId)?.toObjects(StudentUser::class.java)
            result?.forEach {
                cloud.activateQuizForStudent(it.id)
            }
            b.invoke(MyDataState.success("Successful"))
        } catch (e: java.lang.Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun deactivateQuizForStudents(userId: String, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            val result = cloud.getRegisteredStudents(userId)?.toObjects(StudentUser::class.java)
            result?.forEach {
                cloud.deactivateQuizForStudent(it.id)
            }
            b.invoke(MyDataState.success("Successful"))
        } catch (e: java.lang.Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun deleteRegisteredStudents(
        userId: String,
        ids: List<String>,
        b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            ids.forEach { id ->
                cloud.deleteRegisteredStudent(userId, id)
                cloud.unregisterStudent(id)
                cloud.deactivateStudent(id)
            }
            b.invoke(MyDataState.success("Succecssful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun activateStudent(
        id: String, b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            cloud.activateStudent(id)
            b.invoke(MyDataState.success("Succecssful"))
        } catch (e: java.lang.Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun getStudentDetails(id: String, b: (MyDataState<StudentUser>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            val student = cloud.getStudent(id)?.toObject(StudentUser::class.java)
            b.invoke(MyDataState.success(student))
        } catch (e: Exception) {
            b.invoke(MyDataState.error("Check Internet connection and try again", null))
        }
    }

    suspend fun deactivateStudent(
        id: String, b: (MyDataState<String>) -> Unit
    ) {
        b.invoke(MyDataState.loading())
        try {
            cloud.deactivateStudent(id)
            b.invoke(MyDataState.success("Succecssful"))
        } catch (e: java.lang.Exception) {
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
            storage.putFileInStorage(photo.toUri(), userId)
            val uri = storage.getDownloadUri(userId)
            val pic = uri.toString()
            cloud.changeProfilePhoto(userId, pic)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun changeProfileName(userId: String, name: String, b: (MyDataState<String>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            cloud.changeProfileName(userId, name)
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
            cloud.changeProfileEmail(userId, email)
            b.invoke(MyDataState.success("Successful"))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }

    suspend fun getAdmin(userId: String, b: (MyDataState<AdminUser>) -> Unit) {
        b.invoke(MyDataState.loading())
        try {
            val result = cloud.getAdmin(userId)?.toObject(AdminUser::class.java)
            b.invoke(MyDataState.success(result))
        } catch (e: Exception) {
            b.invoke(MyDataState.error(e.toString(), null))
        }
    }
}