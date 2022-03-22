package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.source.ServerUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredUsersViewModel
@Inject constructor(
    private val cloud: AdminCloudData,
    private val auth: ServerUser
) : ViewModel() {

    private val _students = MutableLiveData<MyDataState<List<StudentUser>?>>()
     val students: MutableLiveData<MyDataState<List<StudentUser>?>> get() = _students

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _result = MutableLiveData<String?>()
     val result: LiveData<String?> get() = _result

    init {
        getRegisteredUsers()
    }

     fun getRegisteredUsers() {
        viewModelScope.launch(IO) {
            cloud.getRegisteredStudents(auth.getUserId()) {
                _students.postValue(it)
            }
        }
    }

    fun deleteStudents(ids: List<String>) {
        viewModelScope.launch {
            cloud.deleteRegisteredStudents(auth.getUserId(), ids) {
                when(it.status){
                    Status.LOADING -> {
                        _loading.postValue(true)
                        _result.postValue(it.message)
                    }
                    Status.ERROR -> {
                        _loading.postValue(false)
                        _result.postValue(it.message)
                    }
                    Status.SUCCESS -> {
                        _loading.postValue(false)
                        _result.postValue(it.data)
                    }
                }
            }
        }
    }

    fun activateStudents() {
        viewModelScope.launch {
            cloud.activateQuizForStudents(auth.getUserId()) {
                when(it.status){
                    Status.LOADING -> {
                        _loading.postValue(true)
                        _result.postValue(it.message)
                    }
                    Status.ERROR -> {
                        _loading.postValue(false)
                        _result.postValue(it.message)
                    }
                    Status.SUCCESS -> {
                        _loading.postValue(false)
                        _result.postValue(it.data)
                    }
                }
            }
        }
    }

    fun deactivateStudents() {
        viewModelScope.launch {
            cloud.deactivateQuizForStudents(auth.getUserId()) {
                when(it.status){
                    Status.LOADING -> {
                        _loading.postValue(true)
                        _result.postValue(it.message)
                    }
                    Status.ERROR -> {
                        _loading.postValue(false)
                        _result.postValue(it.message)
                    }
                    Status.SUCCESS -> {
                        _loading.postValue(false)
                        _result.postValue(it.data)
                    }
                }
            }
        }
    }
}