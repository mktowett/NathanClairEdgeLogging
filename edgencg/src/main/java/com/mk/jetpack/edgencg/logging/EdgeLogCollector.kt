package com.mk.jetpack.edgencg.logging

import com.mk.jetpack.edgencg.data.DeviceDataCollector
import javax.inject.Inject


/**
 * EdgeLogCollector is responsible for collecting logs related to device data.
 * It utilizes the DeviceDataCollector to gather necessary device information.
 *
 * @property deviceDataCollector An instance of DeviceDataCollector used to collect device-specific data.
 */
class EdgeLogCollector @Inject constructor(private val deviceDataCollector: DeviceDataCollector) {

    /**
     * Collects logs from the device.
     *
     * @return A map containing key-value pairs of device data.
     */
    fun collectLogs(): Map<String, String> {
        return deviceDataCollector.collectDeviceData()
    }
}