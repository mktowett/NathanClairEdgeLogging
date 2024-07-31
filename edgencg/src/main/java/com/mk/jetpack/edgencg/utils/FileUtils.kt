package com.mk.jetpack.edgencg.utils

import java.io.File
import java.io.FileWriter
import java.io.IOException

object FileUtils {

    fun appendToFile(file: File, text: String) {
        try {
            FileWriter(file, true).use { writer ->
                writer.appendLine(text)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}