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
            remoteConfig.fetchAndActivate().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    actual open fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    actual open suspend fun getFirebaseLogs(path: String): List<String> {
        return try {
            val snapshot = database.child(path).get().await()
            snapshot.children.mapNotNull { it.value?.toString() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    actual open suspend fun getData(path: String): Map<String, Any>? {
        return try {
            val snapshot = database.child(path).get().await()
            snapshot.value as? Map<String, Any>
        } catch (e: Exception) {
            null
        }
    }

    actual open suspend fun registerUserWithEmail(email: String, password: String): String {
        val auth = FirebaseAuth.getInstance()
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Fallo al crear el usuario en Firebase Auth")
    }

    actual open suspend fun loginUserWithEmail(email: String, password: String): String {
        val auth = FirebaseAuth.getInstance()
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Credenciales incorrectas")
    }

    /**
     * Sube una imagen a ImageKit.io.
     * Versión ULTRA-ROBUSTA: Redimensiona la imagen y usa el formato de Auth estricto.
     */
    actual open suspend fun uploadImage(localPath: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val context = FirebaseApp.getInstance().applicationContext
                val uri = android.net.Uri.parse(localPath)
                
                // 1. Cargar y Redimensionar Imagen (Máximo 800px para asegurar éxito total)
                val inputStream = context.contentResolver.openInputStream(uri)
                val original = BitmapFactory.decodeStream(inputStream) ?: throw Exception("Imagen no válida")
                
                val scale = if (original.width > 800) 800f / original.width else 1.0f
                val bitmap = if (scale < 1.0f) {
                    Bitmap.createScaledBitmap(original, (original.width * scale).toInt(), (original.height * scale).toInt(), true)
                } else original
                
                val out = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
                val bytes = out.toByteArray()
                
                if (bitmap != original) bitmap.recycle()
                original.recycle()

                // 2. Auth Basic con el formato que ImageKit espera: "privateKey:"
                val privateKey = ImageKitConfig.PRIVATE_KEY.trim()
                val authString = "$privateKey:"
                val encodedAuth = android.util.Base64.encodeToString(authString.toByteArray(), android.util.Base64.NO_WRAP)
                val authHeader = "Basic $encodedAuth"

                // 3. Petición Multipart Binaria (Sin folder por ahora para descartar errores)
                val fileName = "img_${System.currentTimeMillis()}.jpg"
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName, bytes.toRequestBody("image/jpeg".toMediaTypeOrNull()))
                    .addFormDataPart("fileName", fileName)
                    .addFormDataPart("useUniqueFileName", "true")
                    .build()

                val request = Request.Builder()
                    .url("https://upload.imagekit.io/api/v1/files/upload")
                    .addHeader("Authorization", authHeader)
                    .post(requestBody)
                    .build()

                // 4. Ejecutar
                client.newCall(request).execute().use { response ->
                    val body = response.body?.string() ?: ""
                    Log.d("ImageKit", "Code: ${response.code} - Body: $body")
                    
                    if (response.isSuccessful) {
                        JSONObject(body).getString("url")
                    } else {
                        throw Exception("ImageKit Error ${response.code}: $body")
                    }
                }
            } catch (e: Exception) {
                Log.e("ImageKit", "Error", e)
                throw e
            }
        }
    }

    actual open fun getCurrentUserUid(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
}
