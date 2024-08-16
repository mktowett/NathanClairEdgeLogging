package com.mk.jetpack.edgencg.crash

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mk.jetpack.edgencg.data.DeviceInfoCollector
import com.mk.jetpack.edgencg.logging.LogFileHandler
import com.mk.jetpack.edgencg.logging.LogUploadWorker
import com.mk.jetpack.edgencg.logging.toMap
import com.mk.jetpack.edgencg.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter
import java.util.concurrent.TimeUnit

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 16/08/2024
 * @time    : 10:58
 * @file    : CrashReporter.kt
 */

class CrashReporter {

    companion object {
        fun reportCrash(context: Context, throwable: Throwable) {
            val logFileHandler = LogFileHandler(context)

            // Write the crash stack trace to the log file
            logFileHandler.writeCrashLog(throwable)

            // Trigger a one-time work request to upload the logs
            triggerLogUpload(context)
        }

        private fun triggerLogUpload(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<LogUploadWorker>()
                .setInitialDelay(5, TimeUnit.SECONDS) // Optional delay to give the system time to stabilize
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}