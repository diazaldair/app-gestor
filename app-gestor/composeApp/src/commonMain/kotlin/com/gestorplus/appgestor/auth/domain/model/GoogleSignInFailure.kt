package com.gestorplus.appgestor.auth.domain.model

sealed class GoogleSignInFailure(val errorMessage: String) : Exception(errorMessage) {
    data object UserCancelled : GoogleSignInFailure("El inicio de sesion fue cancelado por el usuario.")
    data object NoCredentialAvailable : GoogleSignInFailure("No se encontraron credenciales de Google en el dispositivo.")
    data object InvalidCredentialType : GoogleSignInFailure("El tipo de credencial recibido no es compatible.")
    data object InvalidGoogleIdToken : GoogleSignInFailure("El token de identidad de Google no es valido.")
    data object NetworkFailure : GoogleSignInFailure("Error de red al intentar conectar con Google.")

    // Firebase Auth errors
    data object AccountExistsWithDifferentCredential : GoogleSignInFailure("Ya existe una cuenta con este correo usando otro metodo de acceso.")
    data object InvalidGoogleCredential : GoogleSignInFailure("La credencial de Google no es valida.")
    data object UserDisabled : GoogleSignInFailure("Esta cuenta de usuario ha sido deshabilitada.")
    data object TooManyRequests : GoogleSignInFailure("Demasiados intentos. Por favor, intentalo mas tarde.")

    // Business/Profile errors
    data object ProfileMissing : GoogleSignInFailure("No se encontro un perfil para este usuario.")
    data object DuplicateRoleProfile : GoogleSignInFailure("Inconsistencia de perfil: el usuario aparece en multiples roles.")
    data object ProfileCreationFailure : GoogleSignInFailure("Error al crear el perfil del usuario.")

    data object UnknownFailure : GoogleSignInFailure("Ocurrio un error inesperado durante el inicio de sesion.")
}
