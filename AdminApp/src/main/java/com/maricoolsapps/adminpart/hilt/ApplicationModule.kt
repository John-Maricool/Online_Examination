package com.maricoolsapps.adminpart.hilt

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.adminpart.room.*
import com.maricoolsapps.adminpart.utils.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.sql.SQLRecoverableException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object ApplicationModule {

    @Singleton
    @Provides
    fun getApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @Singleton
    @Provides
    fun provideMyRoomDatabase(@ApplicationContext context: Context): OnlineDatabase{
        return OnlineDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideDao(myRoomDatabase: OnlineDatabase): RoomDao{
        return myRoomDatabase.dao()
    }

    @Singleton
    @Provides
    fun provideQuizArrangementRepo(dao: RoomDaoImpl): QuizArrangementRepository {
        return QuizArrangementRepository(dao)
    }

    @Singleton
    @Provides
    fun provideCloudMapper(): CloudMapper{
        return CloudMapper()
    }

    @Singleton
    @Provides
    fun provideRoomDaoImpl(dao: RoomDao, mapper: CloudMapper): RoomDaoImpl{
        return RoomDaoImpl(dao, mapper)
    }

    @Singleton
    @Provides
    fun provideServerUser(auth: FirebaseAuth, scope: CoroutineScope): ServerUser{
        return ServerUser(auth, scope)
    }

    @Singleton
    @Provides
    fun provideServerCloudData(cloud: FirebaseFirestore, scope: CoroutineScope, user: ServerUser): ServerCloudData{
        return ServerCloudData(cloud, user, scope)
    }

    @Singleton
    @Provides
    fun provideSavedQuizRepo(dao: RoomDaoImpl): SavedQuizRepository{
        return SavedQuizRepository(dao)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}