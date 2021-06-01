package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.maricoolsapps.adminpart.utils.ServerUser
import javax.inject.Inject

class ChangeNameViewModel
@Inject constructor(val serverUser: ServerUser): ViewModel(){

    fun changeName(newName: String): Task<Void>? {
       return serverUser.changeUsername(newName)
    }
}