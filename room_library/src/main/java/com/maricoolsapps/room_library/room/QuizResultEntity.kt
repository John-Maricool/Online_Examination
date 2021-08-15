package com.maricoolsapps.room_library.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "student_result_entity")
@Parcelize
data class QuizResultEntity(
        @PrimaryKey(autoGenerate = false) val name: String,
        val percentage: Int
)
: Parcelable