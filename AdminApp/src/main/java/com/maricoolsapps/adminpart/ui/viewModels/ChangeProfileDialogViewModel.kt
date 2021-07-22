package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.user.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChangeProfileDialogViewModel
 @Inject constructor(val serverUser: ServerUser, val cloud: AdminCloudData): ViewModel(){

    val name = serverUser.getUserName()
    val email = serverUser.getUserEmail()

    fun changeMail(mail: String): LiveData<MyServerDataState> {
        val state = MutableLiveData<MyServerDataState>()
        viewModelScope.launch(Dispatchers.Main) {
            val job = async(Dispatchers.Default) {serverUser.changeEmail(mail)}
            job.await()
            if (job.isCompleted) {
                val job2 =  async(Dispatchers.Default) { cloud.changeAdminEmail(mail) }
                job2.await()
                if (job2.isCompleted){
                    state.postValue(MyServerDataState.onLoaded)
                }else{
                    state.postValue(MyServerDataState.notLoaded(Exception("Error")))
                }
            }else{
                state.postValue(MyServerDataState.notLoaded(Exception("Error")))
            }
        }
        return state
    }

    fun changeName(newName: String): LiveData<MyServerDataState>{
        val state = MutableLiveData<MyServerDataState>()
        viewModelScope.launch(Dispatchers.Main) {
            val job = async(Dispatchers.Default) {serverUser.changeUsername(newName)
            }
            job.await()
            if (job.isCompleted) {
                val job2 =  async(Dispatchers.Default) {cloud.changeAdminName(newName) }
                job2.await()
                if (job2.isCompleted){
                    state.postValue(MyServerDataState.onLoaded)
                }else{
                    state.postValue(MyServerDataState.notLoaded(Exception("Error")))
                }
            }else{
                state.postValue(MyServerDataState.notLoaded(Exception("Error")))
            }
        }
        return state
    }
}