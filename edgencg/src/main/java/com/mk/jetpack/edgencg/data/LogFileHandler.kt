package com.mk.jetpack.edgencg.data

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mk.jetpack.edgencg.utils.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 31/07/2024
 * @time    : 10:15
 * @file    : LogFileHandler.kt
 */

class LogFileHandler  @Inject constructor(@ApplicationContext private val context: Context) {

    private val logFile: File = File(context.filesDir, "logs.txt")

    fun writeLog(stackTrace: String) {
        FileUtils.appendToFile(logFile, stackTrace)
        if (logFile.length() > MAX_FILE_SIZE) {
            scheduleLogUpload()
        }
    }

    private fun scheduleLogUpload() {
        val uploadWorkRequest = OneTimeWorkRequestBuilder<LogUploadWorker>()
            .setInitialDelay(1, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }

    companion object {
        private const val MAX_FILE_SIZE = 1024 * 1024 // 1 MB
    }
}

