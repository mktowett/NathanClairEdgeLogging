package com.mk.jetpack.nathanclairedgelogging.di

import android.content.Context
import com.mk.jetpack.nathanclairedgelogging.EdgeApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context): EdgeApp {
        return app as EdgeApp
    }
}