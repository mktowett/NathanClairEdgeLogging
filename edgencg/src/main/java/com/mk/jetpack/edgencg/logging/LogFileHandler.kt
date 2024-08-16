package com.mk.jetpack.edgencg.logging

import android.content.Context
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 06/08/2024
 * @time    : 19:13
 * @file    : LogFileHandler.kt
 */
class LogFileHandler(context: Context) {

    private val logFile: File = File(context.filesDir, "app_logs.txt")

    fun writeLog(log: String) {
        try {
            FileWriter(logFile, true).use { writer ->
                writer.append(log).append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getLogFile(): File {
        captureSystemLogs()
        return logFile
    }

    fun hasLogData(): Boolean {
        return logFile.exists() && logFile.length() > 0
    }

    fun clearLogFile() {
        try {
            FileWriter(logFile).use { writer ->
                writer.write("")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun logFileContents() {
        if (logFile.exists()) {
            try {
                BufferedReader(FileReader(logFile)).use { reader ->
                    val log = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        log.append(line).append("\n")
                    }
                    Timber.d("LogFile Contents: \n$log")
                }
            } catch (e: IOException) {
                Timber.e(e, "Failed to read log file contents")
            }
        } else {
            Timber.d("LogFile does not exist")
        }
    }

    private fun captureSystemLogs() {
        try {
            val process = Runtime.getRuntime().exec("logcat -d")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))

            val log = StringBuilder()
            var line: String?

            while (bufferedReader.readLine().also { line = it } != null) {
                log.append(line).append("\n")
            }

            bufferedReader.close()
            writeLog(log.toString())

            // Clear the logcat buffer
            Runtime.getRuntime().exec("logcat -c")
        } catch (e: Exception) {
            Timber.e(e, "Failed to capture system logs")
        }
    }

    // New method to write crash logs
    fun writeCrashLog(throwable: Throwable) {
        try {
            FileWriter(logFile, true).use { writer ->
                val printWriter = PrintWriter(writer)
                printWriter.println("Crash Report:")
                printWriter.println("Time: ${System.currentTimeMillis()}")
                throwable.printStackTrace(printWriter)
                printWriter.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}