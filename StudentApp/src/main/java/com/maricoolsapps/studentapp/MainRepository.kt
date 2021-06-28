package com.maricoolsapps.studentapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.constants
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.StudentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class MainRepository
@Inject constructor(val cloud: StudentCloudData){

    fun registerForQuiz(id: String): LiveData<MyServerDataState>  = cloud.registerForQuiz(id)
    fun checkIfPreviouslyRegistered(): LiveData<Boolean> = cloud.checkIfPreviouslyRegistered()
    fun checkIfAdminDocExist(id: String): LiveData<Boolean> = cloud.checkIfAdminDocExist(id)
}