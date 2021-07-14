package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.LiveData
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import javax.inject.Inject

class RegisteredUsersRepository
@Inject
constructor(var cloud: AdminCloudData) {

    fun getList(): LiveData<MyDataState> = cloud.getRegisteredStudents()

    fun deleteStudents(ids: List<String>) = cloud.deleteRegisteredStudent(ids)

    fun activateStudents(): LiveData<MyServerDataState> = cloud.activateQuizForStudents()

    fun deactivateStudents(): LiveData<MyServerDataState> = cloud.deactivateQuizForStudents()
}