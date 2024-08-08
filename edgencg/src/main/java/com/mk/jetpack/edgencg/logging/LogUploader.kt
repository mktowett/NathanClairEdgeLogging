package com.mk.jetpack.edgencg.logging

import android.content.Context
import com.mk.jetpack.edgencg.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 06/08/2024
 * @time    : 19:14
 * @file    : LogUploader.kt
 */

class LogUploader(private val context: Context) {

    private val logFileHandler = LogFileHandler(context)

    fun upload(logFile: File, deviceInfo: Map<String, RequestBody>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val logFileRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), logFile)
                val logFilePart = MultipartBody.Part.createFormData("logFile", logFile.name, logFileRequestBody)

                val response = RetrofitClient.instance.uploadLogs(logFilePart, deviceInfo)
                if (response.isSuccessful) {
                    logFileHandler.clearLogFile()
                } else {
                    // Handle upload failure
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle upload exception
            }
        }
    }
}