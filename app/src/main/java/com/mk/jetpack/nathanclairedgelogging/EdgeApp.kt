package com.mk.jetpack.nathanclairedgelogging

import android.app.Application
import com.mk.jetpack.edgencg.logging.EdgeNCGLogger


class EdgeApp : Application(){

    lateinit var edgeNCGLogger: EdgeNCGLogger

    override fun onCreate() {
        super.onCreate()
        edgeNCGLogger = EdgeNCGLogger(this)

    }

}