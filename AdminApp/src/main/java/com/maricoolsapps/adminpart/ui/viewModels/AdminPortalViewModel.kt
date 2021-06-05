package com.maricoolsapps.adminpart.ui.viewModels

import androidx.lifecycle.ViewModel
import com.maricoolsapps.utilsandrepository.utils.ServerRepository
import javax.inject.Inject

class AdminPortalViewModel
@Inject constructor(private val serverRepository: ServerRepository): ViewModel(){
    val currentName = serverRepository.currentName
}