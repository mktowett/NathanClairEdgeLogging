package com.mk.jetpack.edgencg.data.model

data class DeviceInfo(
    val reportId: String,
    val appVersionCode: Int,
    val appVersionName: String,
    val packageName: String,
    val filePath: String,
    val phoneModel: String,
    val androidVersion: String,
    val build: BuildInfo,
    val brand: String,
    val product: String,
    val totalMemSize: Long,
    val availableMemSize: Long,
    val userIp: String,
    val userEmail: String,
    val isSilent: Boolean
)