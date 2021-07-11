package com.maricoolsapps.localdatabase.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class RoomEntity(
        @PrimaryKey(autoGenerate = true) var id: Int? = null,
        val question: String,
        val firstOption: String,
        val secondOption: String,
        val thirdOption: String,
        val forthOption: String,
        val correctIndex: Int
)