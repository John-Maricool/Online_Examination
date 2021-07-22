package com.maricoolsapps.adminpart.ui.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(val serverUser: ServerUserRepo, val cloud: AdminCloudData): ViewModel(){

    val profilePhoto: LiveData<Uri?> = serverUser.getProfilePhoto()

    fun changeProfilePhoto(newPhoto: Uri): LiveData<MyServerDataState>{
        val state = MutableLiveData<MyServerDataState>()
        viewModelScope.launch(Main) {
            val job = async(Dispatchers.Default) { serverUser.changeProfilePhoto(newPhoto) }
            job.await()
            if (job.isCompleted) {
               val job2 =  async(Dispatchers.Default) { cloud.changeAdminPhotoUri(newPhoto.toString()) }
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