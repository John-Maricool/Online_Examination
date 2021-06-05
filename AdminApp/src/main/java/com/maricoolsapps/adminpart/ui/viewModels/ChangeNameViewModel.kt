package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.maricoolsapps.utilsandrepository.utils.ServerRepository
import javax.inject.Inject

class ChangeNameViewModel
@Inject constructor(private val serverRepository: ServerRepository): ViewModel(){
    val currentName = serverRepository.currentName
    fun changeName(newName: String) = serverRepository.changeName(newName)
}