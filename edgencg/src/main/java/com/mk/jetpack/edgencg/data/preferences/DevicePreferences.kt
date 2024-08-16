package com.mk.jetpack.edgencg.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID

/**
 * @project :
 * @author  : mktowett
 * @email   : marvintowett@gmail.com
 * @date    : 15/08/2024
 * @time    : 19:35
 * @file    : DevicePreferences.kt
 */

// Extension to create the DataStore instance
private val Context.dataStore by preferencesDataStore("device_prefs")

class DevicePreferences(context: Context) {
    private val dataStore = context.dataStore

    // Function to get the device ID from DataStore
    val deviceId: Flow<String?> = dataStore.data
        .catch { exception ->
            // Handle errors (e.g., IOExceptions) when reading from DataStore
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Retrieve the device ID or return null if it's not set
            preferences[PreferencesKeys.DEVICE_ID_KEY]
        }

    // Function to save the device ID to DataStore
    suspend fun saveDeviceId(id: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEVICE_ID_KEY] = id
        }
    }

    // Function to initialize and store the device ID if it doesn't exist
    suspend fun initDeviceId(): String {
        val existingDeviceId = deviceId.first()

        return if (existingDeviceId == null) {
            // Generate a new UUID for the device ID
            val newDeviceId = UUID.randomUUID().toString()

            // Save the new device ID in DataStore
            saveDeviceId(newDeviceId)

            newDeviceId
        } else {
            // Return the existing device ID
            existingDeviceId
        }
    }
}