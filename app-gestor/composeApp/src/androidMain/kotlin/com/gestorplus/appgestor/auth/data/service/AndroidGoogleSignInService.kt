package com.gestorplus.appgestor.auth.data.service

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.NoCredentialException
import com.gestorplus.appgestor.R
import com.gestorplus.appgestor.auth.domain.model.GoogleSignInFailure
import com.gestorplus.appgestor.auth.domain.service.GoogleSignInService
import com.gestorplus.appgestor.core.util.ActivityProvider
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidGoogleSignInService(
    private val context: Context,
    private val activityProvider: ActivityProvider
) : GoogleSignInService {

    private val credentialManager = CredentialManager.create(context)

    override suspend fun requestGoogleIdToken(): Result<String> = withContext(Dispatchers.Main) {
        val activity = activityProvider.getActivity()
            ?: return@withContext Result.failure(GoogleSignInFailure.UnknownFailure)

        val serverClientId = context.getString(R.string.default_web_client_id)
        if (serverClientId.isBlank()) {
            return@withContext Result.failure(GoogleSignInFailure.UnknownFailure)
        }

        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(serverClientId)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                context = activity,
                request = request
            )

            val credential = result.credential

            if (credential is CustomCredential && 
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                
                if (idToken.isNotEmpty()) {
                    Result.success(idToken)
                } else {
                    Result.failure(GoogleSignInFailure.InvalidGoogleIdToken)
                }
            } else {
                Result.failure(GoogleSignInFailure.InvalidCredentialType)
            }
        } catch (e: GetCredentialCancellationException) {
            Result.failure(GoogleSignInFailure.UserCancelled)
        } catch (e: NoCredentialException) {
            Result.failure(GoogleSignInFailure.NoCredentialAvailable)
        } catch (e: GetCredentialInterruptedException) {
            Result.failure(GoogleSignInFailure.UnknownFailure)
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(GoogleSignInFailure.InvalidGoogleIdToken)
        } catch (e: GetCredentialException) {
            Result.failure(GoogleSignInFailure.UnknownFailure)
        } catch (e: Exception) {
            Result.failure(GoogleSignInFailure.UnknownFailure)
        }
    }
}
