package com.mk.jetpack.edgencg

import com.mk.jetpack.edgencg.logging.EdgeLogCollector
import com.mk.jetpack.edgencg.logging.EdgeLogger
import timber.log.Timber
import javax.inject.Inject

/**
 * EdgeNCG is responsible for initializing logging mechanisms and collecting device data logs.
 * It uses dependency injection to obtain instances of EdgeLogCollector and EdgeLogger for its operations.
 *
 * @property edgeLogCollector An instance of EdgeLogCollector used to collect device data logs.
 * @property edgeLogger An instance of EdgeLogger used for logging the collected device data.
 */
class EdgeNCG @Inject constructor(
    private val edgeLogCollector: EdgeLogCollector,
    private val edgeLogger: EdgeLogger
) {


    /**
     * Initializes the logging framework and collects device data logs.
     * If the application is in debug mode, it plants a Timber DebugTree to enable detailed logging.
     * It then collects device data logs and logs them using the EdgeLogger instance.
     *
     * @param isDebug Indicates whether the application is running in debug mode.
     */
    fun init(isDebug: Boolean) {
        // Initialize Timber
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }

        // Collect device data
        val deviceData = edgeLogCollector.collectLogs()
        edgeLogger.d("Device Data: $deviceData")

    }
}