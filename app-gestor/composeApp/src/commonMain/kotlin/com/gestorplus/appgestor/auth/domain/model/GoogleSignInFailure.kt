package com.gestorplus.appgestor.auth.domain.model

sealed class GoogleSignInFailure(val errorMessage: String) : Exception(errorMessage) {
    data object UserCancelled : GoogleSignInFailure("El inicio de sesion fue cancelado por el usuario.")
    data object NoCredentialAvailable : GoogleSignInFailure("No se encontraron credenciales de Google en el dispositivo.")
    data object InvalidCredentialType : GoogleSignInFailure("El tipo de credencial recibido no es compatible.")
    data object InvalidGoogleIdToken : GoogleSignInFailure("El token de identidad de Google no es valido.")
    data object NetworkFailure : GoogleSignInFailure("Error de red al intentar conectar con Google.")
    data object UnknownFailure : GoogleSignInFailure("Ocurrio un error inesperado durante el inicio de sesion.")
}
