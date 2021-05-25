package com.maricoolsapps.adminpart.appComponents

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.adminpart.QuizArrangementRepository
import com.maricoolsapps.adminpart.room.*
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
    fun provideQuizArrangementRepo(dao: RoomDao): QuizArrangementRepository {
        return QuizArrangementRepository(dao)
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