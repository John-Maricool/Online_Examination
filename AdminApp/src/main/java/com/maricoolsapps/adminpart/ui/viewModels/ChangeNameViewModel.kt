package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.maricoolsapps.adminpart.utils.MyServerDataState
import com.maricoolsapps.adminpart.utils.ServerUser
import com.maricoolsapps.adminpart.utils.ServerUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeNameViewModel
 @Inject constructor(val serverUser: ServerUserRepo): ViewModel(){

    val name = serverUser.getUserName()
    fun changeName(newName: String): LiveData<MyServerDataState> = serverUser.changeUsername(newName)
}