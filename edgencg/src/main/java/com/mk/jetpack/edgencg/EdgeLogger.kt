package com.mk.jetpack.edgencg

import timber.log.Timber

object EdgeLogger {
    fun d(message: String) {
        Timber.d(message)
    }

    fun e(message: String, throwable: Throwable? = null) {
        Timber.e(throwable, message)
    }

    fun i(message: String) {
        Timber.i(message)
    }

    fun w(message: String) {
        Timber.w(message)
    }
}