package com.mk.jetpack.edgencg.data

import android.content.Context
import android.os.Build
import com.mk.jetpack.edgencg.data.model.BuildInfo
import com.mk.jetpack.edgencg.data.model.DeviceInfo
import com.mk.jetpack.edgencg.data.model.VersionInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 31/07/2024
 * @time    : 10:00
 * @file    : DeviceInfoCollector.kt
 */

class DeviceInfoCollector @Inject constructor(@ApplicationContext private val context: Context) {
    fun collect(): DeviceInfo {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val totalMemSize = Runtime.getRuntime().totalMemory()
        val availableMemSize = Runtime.getRuntime().freeMemory()
        val userIp = "N/A" // Placeholder for actual IP collection logic
        val userEmail = "N/A" // Placeholder for actual user email collection logic

        return DeviceInfo(
            reportId = UUID.randomUUID().toString(),
            appVersionCode = packageInfo.versionCode,
            appVersionName = packageInfo.versionName,
            packageName = context.packageName,
            filePath = context.filesDir.absolutePath,
            phoneModel = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            build = collectBuildInfo(),
            brand = Build.BRAND,
            product = Build.PRODUCT,
            totalMemSize = totalMemSize,
            availableMemSize = availableMemSize,
            userIp = userIp,
            userEmail = userEmail,
            isSilent = false // Placeholder for actual silent mode detection
        )
    }

    private fun collectBuildInfo(): BuildInfo {
        return BuildInfo(
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
            isEmulator = Build.FINGERPRINT.startsWith("generic"),
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            permissionsReviewRequired = Build.VERSION.SDK_INT >= 23, // Marshmallow and above
            product = Build.PRODUCT,
            radio = Build.getRadioVersion(),
            supported32BitAbis = Build.SUPPORTED_32_BIT_ABIS.toList(),
            supported64BitAbis = Build.SUPPORTED_64_BIT_ABIS.toList(),
            supportedAbis = Build.SUPPORTED_ABIS.toList(),
            tags = Build.TAGS,
            time = Build.TIME,
            type = Build.TYPE,
            unknown = "unknown",
            user = Build.USER,
            version = collectVersionInfo()
        )
    }

    private fun collectVersionInfo(): VersionInfo {
        return VersionInfo(
            activeCodenames = emptyList(),
            baseOs = Build.VERSION.BASE_OS,
            codename = Build.VERSION.CODENAME,
            firstSdkInt = Build.VERSION_CODES.BASE,
            incremental = Build.VERSION.INCREMENTAL,
            previewSdkFingerprint = "NA",
            previewSdkInt = Build.VERSION.PREVIEW_SDK_INT,
            release = Build.VERSION.RELEASE,
            resourcesSdkInt = 0,
            sdk = Build.VERSION.SDK,
            sdkInt = Build.VERSION.SDK_INT,
            securityPatch = Build.VERSION.SECURITY_PATCH
        )
    }
}