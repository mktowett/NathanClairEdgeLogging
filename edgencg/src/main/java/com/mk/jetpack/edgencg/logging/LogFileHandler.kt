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

    // Append log to the log file
    fun writeLog(log: String) {
        try {
            FileWriter(logFile, true).use { writer ->
                writer.append(log).append("\n")
            }
        } catch (e: IOException) {
            Timber.e(e, "Failed to write log")
        }
    }

    // Return the log file object for further processing
    fun getLogFile(): File {
        return logFile
    }

    // Check if the log file contains any data
    fun hasLogData(): Boolean {
        return logFile.exists() && logFile.length() > 0
    }

    // Clear the log file content
    fun clearLogFile() {
        try {
            FileWriter(logFile).use { writer ->
                writer.write("") // Clear the file by overwriting it with an empty string
            }
        } catch (e: IOException) {
            Timber.e(e, "Failed to clear log file")
        }
    }

    // Stream log file contents line by line to avoid memory issues
    fun streamLogFileContents(lineProcessor: (String) -> Unit) {
        if (logFile.exists()) {
            try {
                BufferedReader(FileReader(logFile)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        lineProcessor(line ?: "")
                    }
                }
            } catch (e: IOException) {
                Timber.e(e, "Failed to stream log file contents")
            }
        } else {
            Timber.d("LogFile does not exist")
        }
    }

    // Check if the log file size exceeds 1MB
    fun isLogFileSizeExceeded(): Boolean {
        // 1MB = 1024 * 1024 bytes
        return logFile.exists() && logFile.length() >= 1 * 1024 * 1024
    }

    // Write crash log details to the log file
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
            Timber.e(e, "Failed to write crash log")
        }
    }
}

