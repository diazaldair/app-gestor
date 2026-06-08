package com.gestorplus.appgestor.core.data.datasource

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

actual open class FirebaseManager actual constructor() {
    private val database = FirebaseDatabase.getInstance("https://appgestor-91a81-default-rtdb.firebaseio.com/").reference
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    actual open suspend fun saveData(path: String, value: String) {
        try {
            database.child(path).setValue(value).await()
        } catch (e: Exception) {
            Log.e("Firebase", "Error: ${e.message}")
        }
    }

    actual open suspend fun initializeRemoteConfig(defaultValues: Map<String, Any>) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(defaultValues).await()
    }

    actual open suspend fun fetchAndActivate(): Boolean {
        return try {
            val updated = remoteConfig.fetchAndActivate().await()
            val allKeys = remoteConfig.all.keys
            Log.d("Firebase_Debug", "Sincronización exitosa. Llaves disponibles: $allKeys")
            true
        } catch (e: Exception) {
            Log.e("Firebase_Debug", "Error en fetch: ${e.message}")
            false
        }
    }

    actual open fun getString(key: String): String {
        val value = remoteConfig.getString(key)
        Log.d("Firebase_Debug", "Leyendo llave [$key]: Valor obtenido -> '$value'")
        return value
    }

    actual open suspend fun getFirebaseLogs(path: String): List<String> {
        return try {
            val snapshot = database.child(path).get().await()
            snapshot.children.mapNotNull { it.value?.toString() }
        } catch (e: Exception) {
            Log.e("Firebase", "Error obteniendo logs: ${e.message}")
            emptyList()
        }
    }

    actual open suspend fun getData(path: String): Map<String, Any>? {
        return try {
            val snapshot = database.child(path).get().await()
            snapshot.value as? Map<String, Any>
        } catch (e: Exception) {
            Log.e("Firebase", "Error obteniendo data: ${e.message}")
            null
        }
    }

    actual open suspend fun registerUserWithEmail(email: String, password: String): String {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Fallo al crear el usuario en Firebase Auth")
    }

    actual open suspend fun loginUserWithEmail(email: String, password: String): String {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Credenciales incorrectas o usuario no encontrado")
    }

    actual open suspend fun uploadImage(localPath: String): String {
        return try {
            val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance().reference
            val fileName = "images/${System.currentTimeMillis()}_${localPath.substringAfterLast("/")}"
            val imageRef = storageRef.child(fileName)
            val uri = android.net.Uri.parse(localPath)
            
            imageRef.putFile(uri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            Log.e("Firebase_Storage", "Error subiendo imagen: ${e.message}")
            throw Exception("Fallo al subir la imagen a Storage")
        }
    }

    actual open fun getCurrentUserUid(): String? {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        return auth.currentUser?.uid
    }
}
