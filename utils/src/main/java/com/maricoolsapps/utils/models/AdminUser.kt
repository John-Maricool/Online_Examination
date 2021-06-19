package com.maricoolsapps.utils.models

import android.net.Uri

data class AdminUser(
        val name: String,
        val email: String,
        val photoUri: String?
){
    constructor(): this("", "", "")
}