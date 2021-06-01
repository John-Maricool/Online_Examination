package com.maricoolsapps.adminpart.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.adminpart.models.RegisteredUsersModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisteredUsersRepository
@Inject
constructor(val cloud: FirebaseFirestore, val auth: FirebaseAuth) {

    lateinit var ans:  MutableList<RegisteredUsersModel>

    suspend fun getListOfRegisteredUsers(): Flow<MyDataState<MutableList<RegisteredUsersModel>>> = flow{
        emit(MyDataState.isLoading)
        try{
            emit(MyDataState.onLoaded(getList()))
        }catch (e: Exception){
            emit(MyDataState.notLoaded(e))
        }
    }

    private fun getList(): MutableList<RegisteredUsersModel> {
     cloud.collection("Admins").document(auth.currentUser.uid).collection("Registered Students").get()
             .addOnCompleteListener {
                 if (it.isSuccessful) {
                      ans = it.result?.toObjects(RegisteredUsersModel::class.java) as MutableList<RegisteredUsersModel>
                     if (ans.isEmpty()){
                         MyDataState.notLoaded(Exception("Error"))
                     }
                 }
                 else{
                     MyDataState.notLoaded(Exception("Error"))
                 }
             }
        return ans
    }
}