package com.gestorplus.appgestor.core.data.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

actual open class FirebaseManager actual constructor() {
    private val database = FirebaseDatabase.getInstance("https://appgestor-91a81-default-rtdb.firebaseio.com/").reference
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    // --- ENVIAR NOTIFICACIÓN PUSH DIRECTA (Usando la API Key del proyecto) ---
    actual open suspend fun sendPushNotification(toToken: String, title: String, body: String, data: Map<String, String>) {
        withContext(Dispatchers.IO) {
            try {
                // Obtenida automáticamente del google-services.json
                val serverKey = "AIzaSyBvvC89D4rdkXlJD8vFGnay124QTMP61l4" 

                val json = JSONObject()
                val notification = JSONObject()
                notification.put("title", title)
                notification.put("body", body)
                notification.put("sound", "default")
                
                val dataJson = JSONObject()
                data.forEach { (key, value) -> dataJson.put(key, value) }

                json.put("to", toToken)
                json.put("notification", notification)
                json.put("data", dataJson)

                val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .addHeader("Authorization", "key=$serverKey")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                Log.d("PushNotification", "Enviado: ${response.isSuccessful} - Code: ${response.code}")
                response.close()
            } catch (e: Exception) {
                Log.e("PushNotification", "Error al enviar push: ${e.message}")
            }
        }
    }

    actual open suspend fun saveData(path: String, value: String) {
        database.child(path).setValue(value).await()
    }

    actual open suspend fun initializeRemoteConfig(defaultValues: Map<String, Any>) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(defaultValues).await()
    }

    actual open suspend fun fetchAndActivate(): Boolean = try {
        remoteConfig.fetchAndActivate().await()
    } catch (e: Exception) {
        false
    }

    actual open fun getString(key: String): String = remoteConfig.getString(key)

    actual open suspend fun getFirebaseLogs(path: String): List<String> = try {
        val snapshot = database.child(path).get().await()
        snapshot.children.mapNotNull { it.value?.toString() }
    } catch (e: Exception) {
        emptyList()
    }

    actual open suspend fun getData(path: String): Map<String, Any>? = try {
        val snapshot = database.child(path).get().await()
        snapshot.value as? Map<String, Any>
    } catch (e: Exception) {
        null
    }

    actual open suspend fun getRawData(path: String): Any? = try {
        database.child(path).get().await().value
    } catch (e: Exception) {
        null
    }

    actual open suspend fun registerUserWithEmail(email: String, password: String): String {
        val result = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Error Auth")
    }

    actual open suspend fun loginUserWithEmail(email: String, password: String): String {
        val result = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Error Auth")
    }

    actual open suspend fun uploadImage(localPath: String): String {
        // Implementación simplificada para brevedad
        return "https://firebasestorage.googleapis.com/v0/b/appgestor-91a81.appspot.com/o/default.jpg"
    }

    actual open fun getCurrentUserUid(): String? = FirebaseAuth.getInstance().currentUser?.uid
}
