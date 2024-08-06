package com.mk.jetpack.nathanclairedgelogging

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.mk.jetpack.edgencg.EdgeLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class EdgeApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var edgeLoggingTree: EdgeLoggingTree

    override fun onCreate() {
        super.onCreate()
        // Initialize Timber with the custom tree
        Timber.plant(edgeLoggingTree)
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}