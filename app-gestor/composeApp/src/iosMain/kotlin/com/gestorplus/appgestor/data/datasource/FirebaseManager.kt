package com.gestorplus.appgestor.core.data.datasource

actual open class FirebaseManager actual constructor() {
    actual open suspend fun saveData(path: String, value: Any) {
        println("Firebase iOS: saveData not yet implemented for $path")
    }

    actual open suspend fun initializeRemoteConfig(defaultValues: Map<String, Any>) {
        println("Firebase iOS: Remote Config initialization not yet implemented")
    }

    actual open suspend fun fetchAndActivate(): Boolean {
        println("Firebase iOS: fetchAndActivate not yet implemented")
        return false
    }

    actual open fun getString(key: String): String {
        println("Firebase iOS: getString not yet implemented")
        return ""
    }

    actual open suspend fun getFirebaseLogs(path: String): List<String> {
        println("Firebase iOS: getFirebaseLogs not yet implemented")
        return emptyList()
    }

    actual open suspend fun getData(path: String): Map<String, Any>? {
        println("Firebase iOS: getData not yet implemented")
        return null
    }

    actual open suspend fun registerUserWithEmail(email: String, password: String): String {
        println("Firebase iOS: registerUserWithEmail not yet implemented")
        throw Exception("Auth no soportada en iOS aún")
    }

    actual open suspend fun loginUserWithEmail(email: String, password: String): String {
        println("Firebase iOS: loginUserWithEmail not yet implemented")
        throw Exception("Auth no soportada en iOS aún")
    }

    actual open suspend fun uploadImage(localPath: String): String {
        println("Firebase iOS: uploadImage not yet implemented")
        throw Exception("Storage no soportado en iOS aún")
    }

    actual open fun getCurrentUserUid(): String? {
        println("Firebase iOS: getCurrentUserUid not yet implemented")
        return null
    }
}
