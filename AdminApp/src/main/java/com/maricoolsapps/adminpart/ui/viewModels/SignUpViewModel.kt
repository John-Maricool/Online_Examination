package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.user.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(val serverUser: ServerUser, val cloud: AdminCloudData): ViewModel(){
    fun signUser(user: AdminUser, auth: FirebaseAuth, password: String): LiveData<MyServerDataState>{
        val state = MutableLiveData<MyServerDataState>()
        viewModelScope.launch(IO) {
            val job = async (IO){serverUser.registerUser(user.email, password, user.name) }

            job.await().let { bool ->
                when(bool){
                    true -> {
                        withContext(Dispatchers.Default) { serverUser.updateProfileName(user.name) }
                        withContext(Dispatchers.Default) { cloud.CreateFirestoreUser(user, auth) }.let { boolean ->
                           when(boolean){
                               true -> {state.postValue(MyServerDataState.onLoaded)}
                               false -> {state.postValue(MyServerDataState.notLoaded(Exception("Error Signing you in")))}
                           }
                       }
                    }
                    false -> {
                        state.postValue(MyServerDataState.notLoaded(Exception("Check your Entries or connections")))
                    }
                }

            }
        }
        return state
    }
}