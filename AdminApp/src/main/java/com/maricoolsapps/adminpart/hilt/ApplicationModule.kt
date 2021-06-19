package com.maricoolsapps.adminpart.hilt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.adminpart.utils.*
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.ServerUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ActivityRetainedComponent::class)

object ApplicationModule {

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
    fun provideServerUserRepo(user: ServerUser) = ServerUserRepo(user)

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
    fun provideServerCloudData(cloud: FirebaseFirestore, scope: CoroutineScope, user: ServerUser): AdminCloudData {
        return AdminCloudData(cloud, user, scope)
    }
}