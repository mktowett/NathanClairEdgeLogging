package com.mk.jetpack.edgencg

import android.util.Log
import com.mk.jetpack.edgencg.data.LogFileHandler
import timber.log.Timber
import javax.inject.Inject

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 31/07/2024
 * @time    : 11:00
 * @file    : EdgeLoggingTree.kt
 */

class EdgeLoggingTree  @Inject constructor(private val logFileHandler: LogFileHandler) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.DEBUG -> logFileHandler.writeLog("[DEBUG] $tag: $message")
            Log.INFO -> logFileHandler.writeLog("[INFO] $tag: $message")
            Log.WARN -> logFileHandler.writeLog("[WARN] $tag: $message")
            Log.ERROR -> {
                logFileHandler.writeLog("[ERROR] $tag: $message")
                t?.let { logFileHandler.writeLog(Log.getStackTraceString(it)) }
            }
        }
    }
}