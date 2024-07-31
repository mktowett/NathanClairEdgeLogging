package com.mk.jetpack.edgencg.data.model

data class VersionInfo(
    val activeCodenames: List<String>,
    val baseOs: String,
    val codename: String,
    val firstSdkInt: Int,
    val incremental: String,
    val previewSdkFingerprint: String,
    val previewSdkInt: Int,
    val release: String,
    val resourcesSdkInt: Int,
    val sdk: String,
    val sdkInt: Int,
    val securityPatch: String
)