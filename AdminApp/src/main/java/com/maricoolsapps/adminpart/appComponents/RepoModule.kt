package com.maricoolsapps.adminpart.appComponents

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.maricoolsapps.localdatabase.room.RoomDao
import com.maricoolsapps.serverdatabase.ServerCloudData
import com.maricoolsapps.serverdatabase.ServerUser
import com.maricoolsapps.utilsandrepository.utils.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideCloudMapper(): CloudMapper{
        return CloudMapper()
    }

    @Singleton
    @Provides
    fun provideRoonDaoImpl(dao: RoomDao, mapper: CloudMapper): RoomDaoImpl{
        return RoomDaoImpl(dao, mapper)
    }

    @Singleton
    @Provides
    fun provideSavedQuizRepo(daoImpl: RoomDaoImpl): SavedQuizRepository{
        return SavedQuizRepository(daoImpl)
    }

    @Singleton
    @Provides
    fun provideQuizArrangementRepo(daoImpl: RoomDaoImpl): QuizArrangementRepository{
        return QuizArrangementRepository(daoImpl)
    }

    @Singleton
    @Provides
    fun provideServerRepo(user: ServerUser): ServerRepository{
        return ServerRepository(user)
    }

    @Singleton
    @Provides
    fun provideUploadQuizRepo(daoImpl: RoomDaoImpl, serverCloudData: ServerCloudData): UploadQuizRepository{
        return UploadQuizRepository(daoImpl, serverCloudData)
    }
}