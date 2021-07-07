package com.maricoolsapps.utils.models

import com.google.firebase.Timestamp

data class QuizSettingModel(val time: Int, val stamp: Timestamp?) {
    constructor(): this(0, null)
}