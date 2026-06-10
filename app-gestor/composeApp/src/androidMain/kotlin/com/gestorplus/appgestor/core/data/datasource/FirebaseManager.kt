package com.gestorplus.appgestor.core.data.datasource

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.gestorplus.appgestor.core.util.ImageKitConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.UUID
import java.util.concurrent.TimeUnit

actual open class FirebaseManager actual constructor() {
    private val database = FirebaseDatabase.getInstance("https://appgestor-91a81-default-rtdb.firebaseio.com/").reference
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

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

    actual open suspend fun uploadImage(localPath: String): String = withContext(Dispatchers.IO) {
        try {
            val context = FirebaseApp.getInstance().applicationContext
            val uri = android.net.Uri.parse(localPath)
            
            val base64Image = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val original = BitmapFactory.decodeStream(inputStream) ?: throw Exception("Formato no soportado")
                val scale = if (original.width > 1000) 1000f / original.width else 1.0f
                val bitmap = if (scale < 1.0f) {
                    Bitmap.createScaledBitmap(original, (original.width * scale).toInt(), (original.height * scale).toInt(), true)
                } else original
                
                val out = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
                val bytes = out.toByteArray()
                if (bitmap != original) bitmap.recycle()
                original.recycle()
                android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
            } ?: throw Exception("Error leyendo archivo")

            val privateKey = ImageKitConfig.PRIVATE_KEY.trim()
            val encodedAuth = android.util.Base64.encodeToString("$privateKey:".toByteArray(), android.util.Base64.NO_WRAP)
            val fileName = "clinic_${System.currentTimeMillis()}.jpg"

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", base64Image)
                .addFormDataPart("fileName", fileName)
                .addFormDataPart("useUniqueFileName", "true")
                .addFormDataPart("folder", "workspaces_gallery") // Sin "/" al inicio
                .build()

            val request = Request.Builder()
                .url("https://upload.imagekit.io/api/v1/files/upload")
                .addHeader("Authorization", "Basic $encodedAuth")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val body = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    JSONObject(body).getString("url")
                } else {
                    Log.e("ImageKit", "Error ${response.code}: $body")
                    throw Exception("ImageKit Error: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseManager", "Upload failed", e)
            throw e
        }
    }

    actual open fun getCurrentUserUid(): String? = FirebaseAuth.getInstance().currentUser?.uid
}
