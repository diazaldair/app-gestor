package com.gestorplus.appgestor.data.datasource

import android.util.Log
import com.gestorplus.appgestor.auth.domain.model.FirebaseAuthenticatedUser
import com.gestorplus.appgestor.auth.domain.model.GoogleSignInFailure
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

actual class FirebaseManager actual constructor() {
    private val database = FirebaseDatabase.getInstance("https://appgestor-91a81-default-rtdb.firebaseio.com/").reference
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    actual suspend fun saveData(path: String, value: String) {
        try {
            database.child(path).setValue(value).await()
        } catch (e: Exception) {
            Log.e("Firebase", "Failed to save data")
        }
    }

    actual suspend fun saveObject(path: String, value: Map<String, Any>) {
        try {
            database.child(path).setValue(value).await()
        } catch (e: Exception) {
            Log.e("Firebase", "Failed to save object")
            throw e
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
            remoteConfig.fetchAndActivate().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    actual fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    actual suspend fun getFirebaseLogs(path: String): List<String> {
        return try {
            val snapshot = database.child(path).get().await()
            snapshot.children.mapNotNull { it.value?.toString() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    actual suspend fun getData(path: String): Map<String, Any>? {
        return try {
            val snapshot = database.child(path).get().await()
            snapshot.value as? Map<String, Any>
        } catch (e: Exception) {
            null
        }
    }

    // Auth
    actual suspend fun registerUserWithEmail(email: String, password: String): String {
        val auth = FirebaseAuth.getInstance()
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Auth failure")
    }

    actual suspend fun loginUserWithEmail(email: String, password: String): String {
        val auth = FirebaseAuth.getInstance()
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Auth failure")
    }

    actual suspend fun signInWithGoogleIdToken(idToken: String): FirebaseAuthenticatedUser {
        if (idToken.isBlank()) throw GoogleSignInFailure.InvalidGoogleIdToken
        
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        
        return try {
            val result = auth.signInWithCredential(credential).await()
            val user = result.user ?: throw GoogleSignInFailure.UnknownFailure
            
            FirebaseAuthenticatedUser(
                uid = user.uid,
                email = user.email ?: "",
                displayName = user.displayName ?: "",
                isNewUser = result.additionalUserInfo?.isNewUser == true
            )
        } catch (e: FirebaseAuthUserCollisionException) {
            if (e.errorCode == "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL") {
                throw GoogleSignInFailure.AccountExistsWithDifferentCredential
            } else {
                throw GoogleSignInFailure.UnknownFailure
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw GoogleSignInFailure.InvalidGoogleCredential
        } catch (e: FirebaseAuthInvalidUserException) {
            throw GoogleSignInFailure.UserDisabled
        } catch (e: FirebaseTooManyRequestsException) {
            throw GoogleSignInFailure.TooManyRequests
        } catch (e: FirebaseNetworkException) {
            throw GoogleSignInFailure.NetworkFailure
        } catch (e: Exception) {
            throw GoogleSignInFailure.UnknownFailure
        }
    }

    // Storage
    actual suspend fun uploadImage(localPath: String): String {
        return try {
            val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance().reference
            val fileName = "images/${System.currentTimeMillis()}_${localPath.substringAfterLast("/")}"
            val imageRef = storageRef.child(fileName)
            val uri = android.net.Uri.parse(localPath)
            
            imageRef.putFile(uri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            throw Exception("Storage failure")
        }
    }

    actual fun getCurrentUserUid(): String? {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser?.uid
    }
}
