package com.mk.jetpack.edgencg

import timber.log.Timber

object Edge {
    fun init() {
        Timber.plant(Timber.DebugTree())
    }

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

    private class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Log to your server for release builds
        }
    }
}