package com.maricoolsapps.adminpart.ui.viewModels

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.models.StudentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisteredUsersDetailViewModel
@Inject constructor(
    private val cloud: AdminCloudData
) : ViewModel() {

    private val _result = MutableLiveData<StudentUser?>()
    val result: LiveData<StudentUser?> get() = _result

    private val _done = MutableLiveData<MyDataState<String>?>()
    val done: LiveData<MyDataState<String>?> get() = _done

    fun getStudent(id: String) {
        viewModelScope.launch {
            cloud.getStudentDetails(id) {
                _result.postValue(it.data)
            }
        }
    }

    fun activateUser(id: String) {
        viewModelScope.launch {
            cloud.activateStudent(id) {
                _done.postValue(it)
            }
        }
    }

    fun deactivateUser(id: String) {
        viewModelScope.launch {
            cloud.deactivateStudent(id) {
                _done.postValue(it)
            }
        }
    }

    fun callStudent(no: String): Intent {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$no")
        return callIntent
    }

}