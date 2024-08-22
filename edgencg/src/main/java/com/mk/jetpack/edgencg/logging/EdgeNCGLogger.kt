package com.mk.jetpack.edgencg.logging

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mk.jetpack.edgencg.Edge
import com.mk.jetpack.edgencg.crash.CrashHandler
import com.mk.jetpack.edgencg.data.preferences.DevicePreferences
import kotlinx.coroutines.runBlocking
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

    init {
        // Initialize Timber
        Edge.init()
        // Initialize the crash handler to capture crashes
        CrashHandler.init(context)
        // Send the first set of logs immediately
        sendLogsImmediately()
        scheduleLogUpload()
    }

    private fun initializeDeviceId() {
        val devicePreferences = DevicePreferences(context)
        // Initialize and store the device ID if it doesn't exist
        runBlocking {
            val deviceId = devicePreferences.initDeviceId()
            println("Device ID initialized: $deviceId") // You can log or store this as needed
        }
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
        val logFileHandler = LogFileHandler(context)

        // Check if the log file size exceeds 1MB
        if (logFileHandler.isLogFileSizeExceeded()) {
            // Trigger immediate upload with OneTimeWorkRequest
            val uploadWorkRequest = OneTimeWorkRequestBuilder<LogUploadWorker>().build()
            WorkManager.getInstance(context).enqueue(uploadWorkRequest)
        }

        // Continue with periodic work to check and upload logs
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicUploadWorkRequest = PeriodicWorkRequestBuilder<LogUploadWorker>(1, TimeUnit.HOURS) // Increased interval to avoid unnecessary frequent checks
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "PeriodicLogUploadWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicUploadWorkRequest
        )
    }
}