package com.gestorplus.appgestor.auth.domain.service

interface GoogleSignInService {
    /**
     * Requests a Google ID Token using the native Credential Manager.
     * @return Result containing the ID Token string or a GoogleSignInFailure.
     */
    suspend fun requestGoogleIdToken(): Result<String>
}
