package com.gestorplus.appgestor.data.datasource

expect class FirebaseManager() {
    suspend fun saveData(path: String, value: String)
    
    // Remote Config
    suspend fun initializeRemoteConfig(defaultValues: Map<String, Any>)
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String): String
    suspend fun getFirebaseLogs(path: String): List<String>
    suspend fun getData(path: String): Map<String, Any>?
}
