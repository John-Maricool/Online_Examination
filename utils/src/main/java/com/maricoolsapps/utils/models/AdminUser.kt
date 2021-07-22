package com.maricoolsapps.utils.models


data class AdminUser(
        val name: String,
        val email: String,
        val photoUri: String?
){
    constructor(): this("", "", null)
}