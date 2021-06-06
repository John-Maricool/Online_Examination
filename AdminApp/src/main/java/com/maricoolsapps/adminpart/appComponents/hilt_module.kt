package com.maricoolsapps.adminpart.appComponents

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object hilt_module {

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideResources(app: Application): Resources = app.resources
}