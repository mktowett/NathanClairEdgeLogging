package com.mk.jetpack.edgencg.logging

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mk.jetpack.edgencg.Edge
import com.mk.jetpack.edgencg.data.DeviceInfoCollector
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 06/08/2024
 * @time    : 19:24
 * @file    : EdgeNCGLogger.kt
 */

class EdgeNCGLogger(private val context: Context) {

    private val logFileHandler = LogFileHandler(context)
    private val deviceInfoCollector = DeviceInfoCollector(context)

    init {
        // Initialize Timber
        Edge.init()
        // Send the first set of logs immediately
        sendLogsImmediately()
        scheduleLogUpload()
    }

    fun logMessage(message: String) {
        Timber.d(message)
        logFileHandler.writeLog(message)
    }

    private fun sendLogsImmediately() {
        val workRequest = OneTimeWorkRequestBuilder<LogUploadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun scheduleLogUpload() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorkRequest = PeriodicWorkRequestBuilder<LogUploadWorker>(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "LogUploadWork",
            ExistingPeriodicWorkPolicy.KEEP,
            uploadWorkRequest
        )
    }
}