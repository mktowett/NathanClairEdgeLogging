package com.mk.jetpack.edgencg.di

import com.mk.jetpack.edgencg.network.LogUploadService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http:74.235.108.48:80/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLogUploadService(retrofit: Retrofit): LogUploadService {
        return retrofit.create(LogUploadService::class.java)
    }

}
