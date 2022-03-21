package com.maricoolsapps.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentUser(
        var id: String,
        var name: String,
        var regNo: String? = null,
        var email: String,
        var number: String? = null,
        val photoUri: String?,
        var isRegistered: Boolean = false,
        var isActivated: Boolean = false,
        val adminId: String? = null,
        val quizScore: Int? = 0
): Parcelable {
        constructor() : this(id = "", name = "", email = "", photoUri = "")
}