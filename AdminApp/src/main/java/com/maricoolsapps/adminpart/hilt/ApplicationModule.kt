package com.maricoolsapps.adminpart.hilt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.maricoolsapps.utils.cloud_data.AdminCloudData
import com.maricoolsapps.utils.source.FirestoreSource
import com.maricoolsapps.utils.source.ServerUser
import com.maricoolsapps.utils.source.StorageSource
import com.maricoolsapps.utils.user.ServerUserRepo
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
    fun provideServerUser(auth: FirebaseAuth) = ServerUser(auth)

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
        return Firebase.firestore
    }

    @ActivityRetainedScoped
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage{
        return FirebaseStorage.getInstance()
    }

    @ActivityRetainedScoped
    @Provides
    fun provideServerCloudData(cloud: FirestoreSource, storage: StorageSource): AdminCloudData {
        return AdminCloudData(cloud, storage)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideFirestoreSource(cloud: FirebaseFirestore) = FirestoreSource(cloud)

}