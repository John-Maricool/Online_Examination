package com.maricoolsapps.studentapp.repos

import com.maricoolsapps.utils.cloud_data.StudentCloudData
import com.maricoolsapps.utils.models.StudentUser
import javax.inject.Inject

class SignUpRepository
@Inject constructor(val cloud: StudentCloudData){

    fun createUser(user: StudentUser) = cloud.CreateFirestoreUser(user)
}