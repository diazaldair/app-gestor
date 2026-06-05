package com.gestorplus.appgestor.data.datasource

actual class FirebaseManager actual constructor() {
    actual suspend fun saveData(path: String, value: String) {
        println("Firebase iOS: saveData not yet implemented for $path")
    }

    actual suspend fun initializeRemoteConfig(defaultValues: Map<String, Any>) {
        println("Firebase iOS: Remote Config initialization not yet implemented")
    }

    actual suspend fun fetchAndActivate(): Boolean {
        println("Firebase iOS: fetchAndActivate not yet implemented")
        return false
    }

    actual fun getString(key: String): String {
        println("Firebase iOS: getString not yet implemented")
        return ""
    }

    actual suspend fun getFirebaseLogs(path: String): List<String> {
        println("Firebase iOS: getFirebaseLogs not yet implemented")
        return emptyList()
    }

    actual suspend fun getData(path: String): Map<String, Any>? {
        println("Firebase iOS: getData not yet implemented")
        return null
    }

    actual suspend fun registerUserWithEmail(email: String, password: String): String {
        println("Firebase iOS: registerUserWithEmail not yet implemented")
        throw Exception("Auth no soportada en iOS aún")
    }

    actual suspend fun loginUserWithEmail(email: String, password: String): String {
        println("Firebase iOS: loginUserWithEmail not yet implemented")
        throw Exception("Auth no soportada en iOS aún")
    }

    actual suspend fun uploadImage(localPath: String): String {
        println("Firebase iOS: uploadImage not yet implemented")
        throw Exception("Storage no soportado en iOS aún")
    }

    actual fun getCurrentUserUid(): String? {
        println("Firebase iOS: getCurrentUserUid not yet implemented")
        return null
    }
}
