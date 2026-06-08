package com.gestorplus.appgestor.core.data.datasource

expect open class FirebaseManager() {
    open suspend fun saveData(path: String, value: String)
    
    // Remote Config
    open suspend fun initializeRemoteConfig(defaultValues: Map<String, Any>)
    open suspend fun fetchAndActivate(): Boolean
    open fun getString(key: String): String
    open suspend fun getFirebaseLogs(path: String): List<String>
    open suspend fun getData(path: String): Map<String, Any>?
    
    // Storage
    open suspend fun uploadImage(localPath: String): String

    // Auth
    open suspend fun registerUserWithEmail(email: String, password: String): String
    open suspend fun loginUserWithEmail(email: String, password: String): String
    open fun getCurrentUserUid(): String?
}
