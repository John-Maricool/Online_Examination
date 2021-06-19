package com.maricoolsapps.adminpart.utils

import androidx.lifecycle.LiveData
import com.maricoolsapps.utils.ServerCloudData
import javax.inject.Inject

class RegisteredUsersRepository
@Inject
constructor(var cloud: ServerCloudData) {

    fun getList(): LiveData<com.maricoolsapps.utils.MyDataState> = cloud.getList()
}