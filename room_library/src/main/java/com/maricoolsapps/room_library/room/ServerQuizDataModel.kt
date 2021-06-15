package com.maricoolsapps.room_library.room


data class ServerQuizDataModel(var question: String,
                               var firstOption: String,
                               var secondOption: String,
                               var thirdOption: String,
                               var forthOption: String,
                               var correctIndex: Int){
    constructor(): this("", "", "", "", "", 0)
}