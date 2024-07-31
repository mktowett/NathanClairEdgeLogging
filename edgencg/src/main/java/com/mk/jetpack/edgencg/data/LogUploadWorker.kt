package com.mk.jetpack.edgencg.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.mk.jetpack.edgencg.data.model.DeviceInfo
import com.mk.jetpack.edgencg.network.LogUploadService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 31/07/2024
 * @time    : 10:21
 * @file    : LogUploadWorker.kt
 */

@HiltWorker
class LogUploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val logUploadService: LogUploadService,
    private val deviceInfoCollector: DeviceInfoCollector
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val logFile = File(applicationContext.filesDir, "logs.txt")
        return try {
            // Implement logic to upload log file to server
            // After successful upload, delete or clear the file
            if (logFile.exists()) {
                // Dummy server upload code (replace with actual implementation)
                // server.upload(logFile)
                logFile.delete()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun uploadLogFile(logFile: File, deviceInfo: DeviceInfo) {
        val requestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), logFile)
        val logFilePart = MultipartBody.Part.createFormData("logFile", logFile.name, requestBody)

        val deviceInfoMap = deviceInfo.toMap()

        val response = logUploadService.uploadLogs(logFilePart, deviceInfoMap)
        if (!response.isSuccessful) {
            throw IOException("Failed to upload log file")
        }
    }

    private fun DeviceInfo.toMap(): Map<String, RequestBody> {
        val gson = Gson()
        val deviceInfoJson = gson.toJson(this)
        val deviceInfoMap = mutableMapOf<String, RequestBody>()
        deviceInfoMap["deviceInfo"] = RequestBody.create("application/json".toMediaTypeOrNull(), deviceInfoJson)
        return deviceInfoMap
    }
}