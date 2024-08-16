package com.mk.jetpack.edgencg.crash

import android.content.Context

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 16/08/2024
 * @time    : 10:57
 * @file    : CrashHandler.kt
 */

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        // Log the crash details to the log file and trigger upload
        CrashReporter.reportCrash(context, throwable)

        // Call the default handler after logging the crash
        defaultHandler?.uncaughtException(thread, throwable)
    }

    companion object {
        // Initialize crash handler
        fun init(context: Context) {
            val crashHandler = CrashHandler(context)
            Thread.setDefaultUncaughtExceptionHandler(crashHandler)
        }
    }
}