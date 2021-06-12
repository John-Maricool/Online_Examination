package com.maricoolsapps.adminpart.appComponents

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.serverdatabase.ServerCloudData
import com.maricoolsapps.serverdatabase.ServerUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object serverDatabaseModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideServerUser(auth: FirebaseAuth): ServerUser{
        return ServerUser(auth)
    }

    @Singleton
    @Provides
    fun provideServerCloudData(cloud: FirebaseFirestore, user: ServerUser): ServerCloudData{
        return ServerCloudData(cloud, user)
    }

}