package com.mk.jetpack.edgencg.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.mk.jetpack.edgencg.Edge
import com.mk.jetpack.edgencg.data.model.DeviceInfo
import com.mk.jetpack.edgencg.network.LogUploadService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject
/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 31/07/2024
 * @time    : 10:21
 * @file    : LogUploadWorker.kt
 */

@HiltWorker
class LogUploadWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters,
    private val logUploadService: LogUploadService,
    private val deviceInfoCollector: DeviceInfoCollector
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val logFile = File(context.filesDir, "logs.txt")
        return@withContext try {
            if (logFile.exists()) {
                val deviceInfo = deviceInfoCollector.collect()
                uploadLogFile(logFile, deviceInfo)
                logFile.delete()
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to upload log file")
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