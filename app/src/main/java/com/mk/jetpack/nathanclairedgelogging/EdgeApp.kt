package com.mk.jetpack.nathanclairedgelogging

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EdgeApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}