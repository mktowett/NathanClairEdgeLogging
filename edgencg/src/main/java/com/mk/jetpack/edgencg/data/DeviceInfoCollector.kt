package com.mk.jetpack.edgencg.data

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.mk.jetpack.edgencg.data.model.BuildInfo
import com.mk.jetpack.edgencg.data.model.DeviceInfo
import com.mk.jetpack.edgencg.data.model.VersionInfo
import java.io.BufferedReader
import java.io.FileReader
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.UUID

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 06/08/2024
 * @time    : 19:26
 * @file    : DeviceInfoCollector.kt
 */

class DeviceInfoCollector(private val context: Context) {

    fun collect(): DeviceInfo {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val appVersionCode = packageInfo.versionCode
        val appVersionName = packageInfo.versionName

        val buildInfo = BuildInfo(
            board = Build.BOARD,
            bootloader = Build.BOOTLOADER,
            brand = Build.BRAND,
            cpuAbi = Build.CPU_ABI,
            cpuAbi2 = Build.CPU_ABI2,
            device = Build.DEVICE,
            display = Build.DISPLAY,
            fingerprint = Build.FINGERPRINT,
            hardware = Build.HARDWARE,
            host = Build.HOST,
            id = Build.ID,
            isDebuggable = false,
            isEmulator = isEmulator(),
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            permissionsReviewRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M,
            product = Build.PRODUCT,
            radio = Build.getRadioVersion(),
            supported32BitAbis = Build.SUPPORTED_32_BIT_ABIS.asList(),
            supported64BitAbis = Build.SUPPORTED_64_BIT_ABIS.asList(),
            supportedAbis = Build.SUPPORTED_ABIS.asList(),
            tags = Build.TAGS,
            time = Build.TIME,
            type = Build.TYPE,
            unknown = Build.UNKNOWN,
            user = Build.USER,
            version = VersionInfo(
                activeCodenames = listOf(Build.VERSION.CODENAME),
                baseOs = Build.VERSION.BASE_OS,
                codename = Build.VERSION.CODENAME,
                firstSdkInt = Build.VERSION.SDK_INT,
                incremental = Build.VERSION.INCREMENTAL,
                previewSdkFingerprint = "NA",
                previewSdkInt = Build.VERSION.PREVIEW_SDK_INT,
                release = Build.VERSION.RELEASE,
                resourcesSdkInt = 0,
                sdk = Build.VERSION.SDK,
                sdkInt = Build.VERSION.SDK_INT,
                securityPatch = Build.VERSION.SECURITY_PATCH
            )
        )

        val totalMemSize = getTotalMemory()
        val availableMemSize = getAvailableMemory()
        val userIp = getUserIp()
        val userEmail = "NA"
        val isSilent = false

        return DeviceInfo(
            reportId = UUID.randomUUID().toString(),
            appVersionCode = appVersionCode,
            appVersionName = appVersionName,
            packageName = packageName,
            filePath = context.filesDir.absolutePath,
            phoneModel = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            build = buildInfo,
            brand = Build.BRAND,
            product = Build.PRODUCT,
            totalMemSize = totalMemSize,
            availableMemSize = availableMemSize,
            userIp = userIp,
            userEmail = userEmail,
            isSilent = isSilent
        )
    }

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("google/sdk_gphone_")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    }

    private fun getTotalMemory(): Long {
        val reader = BufferedReader(FileReader("/proc/meminfo"))
        val totalMem = reader.useLines { lines ->
            lines.firstOrNull { it.startsWith("MemTotal:") }
                ?.replace(Regex("\\D+"), "")
                ?.toLongOrNull()
        }
        return totalMem ?: 0L
    }

    private fun getAvailableMemory(): Long {
        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem
    }

    private fun getUserIp(): String {
        try {
            val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                val addresses: List<InetAddress> = Collections.list(networkInterface.inetAddresses)
                for (address in addresses) {
                    if (!address.isLoopbackAddress) {
                        return address.hostAddress
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "0.0.0.0"
    }


}