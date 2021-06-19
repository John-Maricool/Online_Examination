package com.maricoolsapps.utils.models


data class StudentUser(
        val name: String,
        val email: String,
        val number: String?,
        val quizStatus: String
){
    constructor(): this("", "", "", isAnswered)

    companion object{
        val isAnswered = "Answered"
        val notAnswered = "unAnswered"
    }
}