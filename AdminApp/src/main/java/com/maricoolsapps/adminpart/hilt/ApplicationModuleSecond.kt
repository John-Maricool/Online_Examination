package com.maricoolsapps.adminpart.hilt

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.adminpart.utils.*
import com.maricoolsapps.room_library.room.CloudMapper
import com.maricoolsapps.room_library.room.OnlineDatabase
import com.maricoolsapps.room_library.room.RoomDao
import com.maricoolsapps.room_library.room.RoomDaoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object ApplicationModuleSecond {

    @Singleton
    @Provides
    fun provideMyRoomDatabase(@ApplicationContext context: Context): OnlineDatabase {
        return OnlineDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideDao(myRoomDatabase: OnlineDatabase): RoomDao {
        return myRoomDatabase.dao()
    }

    @Singleton
    @Provides
    fun provideCloudMapper(): CloudMapper {
        return CloudMapper()
    }

    @Singleton
    @Provides
    fun provideRoomDaoImpl(dao: RoomDao, mapper: CloudMapper): RoomDaoImpl {
        return RoomDaoImpl(dao, mapper)
    }

    @Singleton
    @Provides
    fun provideQuizArrangementRepo(dao: RoomDaoImpl): QuizArrangementRepository {
        return QuizArrangementRepository(dao)
    }

    @Singleton
    @Provides
    fun provideSavedQuizRepo(dao: RoomDaoImpl): SavedQuizRepository{
        return SavedQuizRepository(dao)
    }


}