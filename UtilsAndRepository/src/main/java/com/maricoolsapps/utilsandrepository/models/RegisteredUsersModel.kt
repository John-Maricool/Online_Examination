package com.maricoolsapps.utilsandrepository.models

data class RegisteredUsersModel(
        val uid: String,
        val name: String,
        val email: String,
        val phoneNumber: String
){
    constructor(): this("", "", "", "")
}