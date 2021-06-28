package com.maricoolsapps.studentapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.utils.ServerUser
import com.maricoolsapps.utils.cloud_data.StudentCloudData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ActivityRetainedComponent::class)

object StudentApplicationModule {

    @ActivityRetainedScoped
    @Provides
    fun getApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @ActivityRetainedScoped
    @Provides
    fun provideServerUser(auth: FirebaseAuth, scope: CoroutineScope) = ServerUser(auth, scope)

    @ActivityRetainedScoped
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideServerCloudData(cloud: FirebaseFirestore, scope: CoroutineScope, user: ServerUser): StudentCloudData {
        return StudentCloudData(cloud, user, scope)
    }
}