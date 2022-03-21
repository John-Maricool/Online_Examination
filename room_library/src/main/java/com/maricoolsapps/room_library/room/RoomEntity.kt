package com.maricoolsapps.room_library.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "question_table")
@Parcelize
data class RoomEntity(
        @PrimaryKey(autoGenerate = true) var id: Int? = null,
        val question: String,
        val firstOption: String,
        val image: String? = null,
        val secondOption: String,
        val thirdOption: String,
        val forthOption: String,
        val correctIndex: Int
) : Parcelable