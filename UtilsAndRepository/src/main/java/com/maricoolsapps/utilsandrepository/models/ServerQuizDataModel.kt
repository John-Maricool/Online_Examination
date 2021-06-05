package com.maricoolsapps.utilsandrepository.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServerQuizDataModel(var question: String,
                               var firstOption: String,
                               var secondOption: String,
                               var thirdOption: String,
                               var forthOption: String,
                               var correctIndex: Int): Parcelable{
    constructor(): this("", "", "", "", "", 0)
}