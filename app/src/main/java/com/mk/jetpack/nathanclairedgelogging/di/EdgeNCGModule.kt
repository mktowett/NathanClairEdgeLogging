package com.mk.jetpack.nathanclairedgelogging.di

import android.content.Context
import com.mk.jetpack.edgencg.data.DeviceDataCollector
import com.mk.jetpack.edgencg.logging.EdgeLogCollector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EdgeNCGModule {

    @Provides
    @Singleton
    fun provideDeviceDataCollector(@ApplicationContext context: Context): DeviceDataCollector {
        return DeviceDataCollector(context)
    }

    @Provides
    @Singleton
    fun provideLogCollector(deviceDataCollector: DeviceDataCollector): EdgeLogCollector {
        return EdgeLogCollector(deviceDataCollector)
    }
}