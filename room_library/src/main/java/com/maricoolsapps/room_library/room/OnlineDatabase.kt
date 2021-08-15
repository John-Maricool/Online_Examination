package com.maricoolsapps.room_library.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity::class, QuizResultEntity::class], version = 1, exportSchema = false)
abstract class OnlineDatabase: RoomDatabase() {
    abstract fun dao(): RoomDao

    companion object{
        @Volatile
        var INSTANCE: OnlineDatabase? = null

        fun getDatabase(
                context: Context
        ): OnlineDatabase {
            return INSTANCE
                    ?: synchronized(this){
                        val instance = Room.databaseBuilder(
                                context.applicationContext,
                                OnlineDatabase::class.java,
                                "my_database"
                        )
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build()
                        INSTANCE = instance
                        instance
                    }
        }
    }
}