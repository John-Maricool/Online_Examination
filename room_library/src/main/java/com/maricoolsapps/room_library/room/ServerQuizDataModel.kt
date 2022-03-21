package com.maricoolsapps.room_library.room

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServerQuizDataModel(var question: String,
                               var firstOption: String,
                               val image: String? = null,
                               var secondOption: String,
                               var thirdOption: String,
                               var forthOption: String,
                               var correctIndex: Int) : Parcelable {
    constructor(): this("", "", null, "", "", "", 0)
}