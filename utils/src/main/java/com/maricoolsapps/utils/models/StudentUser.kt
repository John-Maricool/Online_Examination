package com.maricoolsapps.utils.models

data class StudentUser(
        var id: String,
        var name: String,
        var regNo: String?,
        var email: String,
        var number: String,
        var isRegistered: Boolean,
        var isActivated: Boolean
){
    constructor(): this("", "", null, "", "", false, true)
}