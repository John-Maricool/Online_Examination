package com.maricoolsapps.utils.source

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.models.PersonId
import com.maricoolsapps.utils.models.QuizSettingModel
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.constants
import com.maricoolsapps.utils.others.constants.collectionName
import com.maricoolsapps.utils.others.constants.quizDocs
import com.maricoolsapps.utils.others.constants.quizTime
import com.maricoolsapps.utils.others.constants.settings
import com.maricoolsapps.utils.others.constants.studentsCollectionName
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirestoreSource
@Inject constructor(var cloud: FirebaseFirestore) {

    suspend fun setFirestoreAdmin(user: AdminUser): Void? {
        return cloud.collection(collectionName).document(user.userID).set(user).await()
    }

    suspend fun setFirestoreStudent(user: StudentUser): Void? {
        return cloud.collection(constants.studentsCollectionName).document(user.id).set(user)
            .await()
    }

    suspend fun registerStudentForQuiz(adminId: String, userId: String): Void? {
        return cloud.collection(constants.studentsCollectionName)
            .document(userId)
            .update(
                "adminId",
                adminId,
                "registered",
                true,
                "quizScore",
                null,
                "activated",
                false
            )
            .await()
    }

    suspend fun addStudentToAdminList(adminId: String, studentId: String): Void? {
        val student = PersonId(studentId)
        return cloud.collection(collectionName).document(adminId)
            .collection(constants.registeredStudents).document(studentId)
            .set(student).await()
    }

    suspend fun checkIfAdminDocExist(id: String): Boolean {
        return try {
            val res = cloud.collection(collectionName).document(id).get(Source.SERVER).await()
            res != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllQuizDocs(userId: String): QuerySnapshot? {
        return cloud.collection(collectionName).document(userId)
            .collection(constants.quizDocs).get().await()
    }

    suspend fun addQuizQuestionsToDb(userId: String, data: List<Any>) {
        return data.forEach {
            cloud.collection(collectionName).document(userId)
                .collection(constants.quizDocs).add(it).await()
        }
    }

    suspend fun getStudent(userId: String): DocumentSnapshot? {
        return cloud.collection(constants.studentsCollectionName)
            .document(userId).get().await()
    }


    suspend fun addQuizTime(userId: String, time: QuizSettingModel): Void? {
        return cloud.collection(collectionName)
            .document(userId).collection(constants.settings)
            .document(constants.quizTime).set(time).await()
    }

    suspend fun getRegisteredStudents(userID: String): QuerySnapshot? {
        return cloud.collection(collectionName)
            .document(userID).collection(constants.registeredStudents)
            .get(Source.SERVER).await()
    }

    suspend fun activateQuizForStudent(userId: String) {
        cloud.collection(constants.studentsCollectionName).document(userId)
            .update("activated", true).await()
    }

    suspend fun deactivateQuizForStudent(userId: String) {
        cloud.collection(constants.studentsCollectionName).document(userId)
            .update("activated", false).await()
    }

    suspend fun deleteRegisteredStudent(userId: String, studentId: String): Void? {
        return cloud.collection(collectionName)
            .document(userId).collection(constants.registeredStudents)
            .document(studentId).delete().await()
    }

    suspend fun unregisterStudent(studentId: String): Void? {
        return cloud.collection(studentsCollectionName).document(studentId)
            .update("registered", false, "quizScore", 0).await()
    }

    suspend fun activateStudent(studentId: String): Void? {
        return cloud.collection(studentsCollectionName).document(studentId)
            .update("activated", true)
            .await()
    }

    suspend fun deactivateStudent(studentId: String): Void? {
        return cloud.collection(studentsCollectionName).document(studentId)
            .update("activated", false)
            .await()
    }

    suspend fun updateQuizResult(score: Int, userId: String): Void? {
        return cloud.collection(studentsCollectionName)
            .document(userId).update("quizScore", score).await()
    }

    suspend fun changeProfilePhoto(userId: String, photo: String): Void? {
        return cloud.collection(collectionName).document(userId).update("photoUri", photo).await()
    }

    suspend fun changeProfileName(userId: String, name: String): Void? {
        return cloud.collection(collectionName).document(userId).update("name", name).await()
    }

    suspend fun changeProfileEmail(userId: String, email: String): Void? {
        return cloud.collection(collectionName).document(userId).update("email", email).await()
    }

    suspend fun getAdmin(userId: String): DocumentSnapshot? {
        return cloud.collection(collectionName).document(userId).get().await()
    }

    suspend fun changeStudentProfilePhoto(userId: String, photo: String): Void? {
        return cloud.collection(studentsCollectionName).document(userId).update("photoUri", photo)
            .await()
    }

    suspend fun changeStudentProfileName(userId: String, name: String): Void? {
        return cloud.collection(studentsCollectionName).document(userId).update("name", name)
            .await()
    }

    suspend fun changeStudentProfileEmail(userId: String, email: String): Void? {
        return cloud.collection(studentsCollectionName).document(userId).update("email", email)
            .await()
    }

    suspend fun getAllQuizDocId(studentId: String): MutableList<DocumentSnapshot> {
        val adminID =
            cloud.collection(studentsCollectionName).document(studentId).get().await()["adminId"]
        return cloud.collection(collectionName).document(adminID as String).collection(quizDocs)
            .get().await().documents
    }

    suspend fun getQuizQuestion(studentId: String, docId: String): DocumentSnapshot? {
        val adminID =
            cloud.collection(studentsCollectionName).document(studentId).get().await()["adminId"]
        return cloud.collection(collectionName).document(adminID as String).collection(quizDocs)
            .document(docId).get().await()
    }

    suspend fun getQuizTime(studentId: String): DocumentSnapshot? {
        val adminID =
            cloud.collection(studentsCollectionName).document(studentId).get().await()["adminId"]
        return cloud.collection(collectionName).document(adminID as String).collection(settings)
            .document(
                quizTime
            ).get().await()
    }
}