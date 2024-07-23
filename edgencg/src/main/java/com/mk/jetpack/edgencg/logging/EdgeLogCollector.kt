package com.mk.jetpack.edgencg.logging

import com.mk.jetpack.edgencg.data.DeviceDataCollector
import javax.inject.Inject

class EdgeLogCollector @Inject constructor(private val deviceDataCollector: DeviceDataCollector) {

    fun collectLogs(): Map<String, String> {
        return deviceDataCollector.collectDeviceData()
    }
}