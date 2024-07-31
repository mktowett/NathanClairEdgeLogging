package com.mk.jetpack.edgencg.di

import android.content.Context
import com.mk.jetpack.edgencg.data.DeviceInfoCollector
import com.mk.jetpack.edgencg.data.LogFileHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

    @Provides
    @Singleton
    fun provideDeviceInfoCollector(@ApplicationContext context: Context): DeviceInfoCollector {
        return DeviceInfoCollector(context)
    }

    @Provides
    @Singleton
    fun provideLogFileHandler(@ApplicationContext context: Context): LogFileHandler {
        return LogFileHandler(context)
    }
}