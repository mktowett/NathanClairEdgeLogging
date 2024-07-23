package com.mk.jetpack.edgencg.data

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Collects various pieces of device data for analytics or diagnostic purposes.
 * This class leverages Android's system services to gather device-specific information.
 *
 * @property context The application context used to access system services and application-specific data.
 */

@Singleton
class DeviceDataCollector @Inject constructor(
    @ApplicationContext private val context: Context
){


    /**
     * Collects a comprehensive set of device data.
     *
     * @return A map containing key-value pairs of device information, including manufacturer, model,
     * OS version, app version, device ID, network type, battery level, screen resolution, and locale.
     */
    fun collectDeviceData(): Map<String, String> {
        val deviceData = mutableMapOf<String, String>()

        // Manufacturer
        deviceData["Manufacturer"] = Build.MANUFACTURER

        // Model
        deviceData["Model"] = Build.MODEL

        // OS Version
        deviceData["OS Version"] = Build.VERSION.RELEASE

        // App Version
        deviceData["App Version"] = context.packageManager.getPackageInfo(context.packageName, 0).versionName

        // Device ID
        deviceData["Device ID"] = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        // Network Type
        deviceData["Network Type"] = getNetworkType()

        // Battery Level
        deviceData["Battery Level"] = getBatteryLevel().toString()

        // Screen Resolution
        deviceData["Screen Resolution"] = "${Resources.getSystem().displayMetrics.widthPixels}x${Resources.getSystem().displayMetrics.heightPixels}"

        // Locale
        deviceData["Locale"] = Locale.getDefault().toString()

        return deviceData
    }

    /**
     * Determines the type of network connection currently in use.
     *
     * @return A string representing the type of network connection (e.g., WIFI, CELLULAR, ETHERNET, or Unknown).
     */
    private fun getNetworkType(): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return "Unknown"
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return "Unknown"

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
            else -> "Unknown"
        }
    }

    /**
     * Retrieves the current battery level of the device.
     *
     * @return An integer representing the current battery level percentage.
     */
    private fun getBatteryLevel(): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}