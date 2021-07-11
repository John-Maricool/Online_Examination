package com.maricoolsapps.adminpart.appComponents

import android.content.Context
import com.maricoolsapps.localdatabase.room.OnlineDatabase
import com.maricoolsapps.localdatabase.room.RoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object localDatabaseModule {

    @Singleton
    @Provides
    fun provideMyRoomDatabase(@ApplicationContext context: Context): OnlineDatabase {
        return OnlineDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun getApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @Singleton
    @Provides
    fun provideDao(myRoomDatabase: OnlineDatabase): RoomDao {
        return myRoomDatabase.dao()
    }

}