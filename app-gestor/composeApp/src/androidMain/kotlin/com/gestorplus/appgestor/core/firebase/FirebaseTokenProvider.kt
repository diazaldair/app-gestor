package com.gestorplus.appgestor.core.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Implementación 'actual' para Android. 
 * Esta función es la que realmente habla con los servidores de Google.
 */
actual suspend fun getToken(): String = suspendCoroutine { continuation ->
    // 1. Obtenemos la instancia de Firebase Messaging
    FirebaseMessaging.getInstance().token
        .addOnCompleteListener { task ->
            // 2. Verificamos si la tarea falló (ej: falta de internet o Google Play Services desactualizados)
            if (!task.isSuccessful) {
                Log.w("FIREBASE_DEBUG", "Fallo al obtener el token", task.exception)
                // Avisamos a la corrutina que hubo un error
                continuation.resumeWithException(task.exception ?: Exception("Error desconocido"))
                return@addOnCompleteListener
            }

            // 3. Si todo salió bien, extraemos el token (el "ID de correo" del dispositivo)
            val token = task.result
            Log.d("FIREBASE_DEBUG", "Token obtenido con éxito: $token")

            // 4. Entregamos el resultado a la corrutina para que continúe su ejecución
            continuation.resume(token ?: "")
        }
}
