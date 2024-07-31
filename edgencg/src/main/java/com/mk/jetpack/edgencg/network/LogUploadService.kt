package com.mk.jetpack.edgencg.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface LogUploadService {

    @Multipart
    @POST("upload/logs")
    suspend fun uploadLogs(
        @Part logFile: MultipartBody.Part,
        @PartMap deviceInfo: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<Void>
}