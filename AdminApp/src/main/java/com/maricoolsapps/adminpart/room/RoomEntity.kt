package com.maricoolsapps.adminpart.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class RoomEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val question: String,
        val firstOption: String,
        val secondOption: String,
        val thirdOption: String,
        val forthOption: String,
        val correctIndex: Int
)