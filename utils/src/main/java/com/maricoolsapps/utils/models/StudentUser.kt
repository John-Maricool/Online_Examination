package com.maricoolsapps.utils.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentUser(
        var id: String,
        var name: String,
        var regNo: String?,
        var email: String,
        var number: String,
        var isRegistered: Boolean,
        var isActivated: Boolean,
        val adminId: String?,
        val quizScore: Int?
): Parcelable{
    constructor(): this("", "", null, "", "", false, false, null, null)
}