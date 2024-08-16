package com.mk.jetpack.edgencg.logging

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mk.jetpack.edgencg.data.DeviceInfoCollector
import com.mk.jetpack.edgencg.data.model.DeviceInfo
import com.mk.jetpack.edgencg.data.preferences.DevicePreferences
import com.mk.jetpack.edgencg.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.File

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 06/08/2024
 * @time    : 19:50
 * @file    : LogUploadWorker.kt
 */

class LogUploadWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val logFileHandler = LogFileHandler(context)
    private val deviceInfoCollector = DeviceInfoCollector(context)
    private val devicePreferences = DevicePreferences(context)

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                // Fetch the device ID asynchronously
                val deviceId = devicePreferences.deviceId.first()

                if (deviceId == null) {
                    Timber.e("Device ID not found")
                    return@withContext Result.failure()
                }

                if (logFileHandler.hasLogData()) {
                    logFileHandler.logFileContents() // Log the contents of the log file
                    val logFile = logFileHandler.getLogFile()
                    val deviceInfo = deviceInfoCollector.collect()
                    val deviceInfoMap = deviceInfo.toMap().toMutableMap()

                    // Add device ID and timestamp to the report
                    deviceInfoMap["deviceReportId"] = deviceId.toRequestBody("text/plain".toMediaTypeOrNull())
                    deviceInfoMap["timestamp"] = System.currentTimeMillis().toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    val result = uploadLogs(logFile, deviceInfoMap)
                    if (result) {
                        Result.success()
                    } else {
                        Result.retry()
                    }
                } else {
                    Timber.i("No log data to upload.")
                    Result.success()
                }
            } catch (e: Exception) {
                Timber.e(e, "Log upload failed.")
                Result.retry()
            }
        }
    }

    private suspend fun uploadLogs(logFile: File, deviceInfoMap: Map<String, RequestBody>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val logFileRequestBody = logFile.asRequestBody("text/plain".toMediaTypeOrNull())
                val logFilePart = MultipartBody.Part.createFormData("logFile", logFile.name, logFileRequestBody)

                val response: Response<Void> = RetrofitClient.instance.uploadLogs(logFilePart, deviceInfoMap)
                if (response.isSuccessful) {
                    Timber.i("Log file uploaded successfully.")
                    logFileHandler.clearLogFile()
                    true
                } else {
                    Timber.e("Failed to upload log file. Response code: ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception during log file upload.")
                false
            }
        }
    }
}

fun DeviceInfo.toMap(): Map<String, RequestBody> {
    val map = mutableMapOf<String, RequestBody>()
    map["appVersionCode"] = appVersionCode.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    map["appVersionName"] = appVersionName.toRequestBody("text/plain".toMediaTypeOrNull())
    map["packageName"] = packageName.toRequestBody("text/plain".toMediaTypeOrNull())
    map["phoneModel"] = phoneModel.toRequestBody("text/plain".toMediaTypeOrNull())
    map["androidVersion"] = androidVersion.toRequestBody("text/plain".toMediaTypeOrNull())
    map["brand"] = brand.toRequestBody("text/plain".toMediaTypeOrNull())
    map["product"] = product.toRequestBody("text/plain".toMediaTypeOrNull())
    map["totalMemSize"] = totalMemSize.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    map["availableMemSize"] = availableMemSize.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    map["userIp"] = userIp.toRequestBody("text/plain".toMediaTypeOrNull())
    map["userEmail"] = userEmail.toRequestBody("text/plain".toMediaTypeOrNull())
    map["isSilent"] = isSilent.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    return map
}