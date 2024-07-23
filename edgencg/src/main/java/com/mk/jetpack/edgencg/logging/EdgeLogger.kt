package com.mk.jetpack.edgencg.logging

import timber.log.Timber

/**
 * A logging utility class that wraps around Timber's logging functions.
 * This class provides simplified methods for logging various levels of information.
 */
class EdgeLogger {

    /**
     * Logs a debug message.
     *
     * @param message The message to log.
     */
    fun d(message: String) {
        Timber.d(message)
    }

    /**
     * Logs an error message. Optionally, a throwable can be provided for logging the stack trace.
     *
     * @param message The message to log.
     * @param throwable An optional Throwable to log, null by default.
     */
    fun e(message: String, throwable: Throwable? = null) {
        Timber.e(throwable, message)
    }

    /**
     * Logs an informational message.
     *
     * @param message The message to log.
     */
    fun i(message: String) {
        Timber.i(message)
    }

    /**
     * Logs a warning message.
     *
     * @param message The message to log.
     */
    fun w(message: String) {
        Timber.w(message)
    }
}