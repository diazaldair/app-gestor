package com.gestorplus.appgestor.data.datasource

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

actual class FirebaseManager actual constructor() {
    private val database = FirebaseDatabase.getInstance().reference
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    actual suspend fun saveData(path: String, value: String) {
        try {
            database.child(path).setValue(value).await()
        } catch (e: Exception) {
            Log.e("Firebase", "Error: ${e.message}")
        }
    }

    actual suspend fun initializeRemoteConfig(defaultValues: Map<String, Any>) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(defaultValues).await()
    }

    actual suspend fun fetchAndActivate(): Boolean {
        return try {
            val updated = remoteConfig.fetchAndActivate().await()
            // Imprimimos TODAS las llaves que Firebase conoce ahora mismo
            val allKeys = remoteConfig.all.keys
            Log.d("Firebase_Debug", "Sincronización exitosa. Llaves disponibles: $allKeys")
            true
        } catch (e: Exception) {
            Log.e("Firebase_Debug", "Error en fetch: ${e.message}")
            false
        }
    }

    actual fun getString(key: String): String {
        val value = remoteConfig.getString(key)
        Log.d("Firebase_Debug", "Leyendo llave [$key]: Valor obtenido -> '$value'")
        return value
    }
}
