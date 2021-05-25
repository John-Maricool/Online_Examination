package com.maricoolsapps.adminpart

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class ServerQuizData(var question: String,
                     var firstOption: String,
                     var secondOption: String,
                     var thirdOption: String,
                     var forthOption: String,
                     var correctIndex: Int){
    constructor(): this("", "", "", "", "", 0)
}