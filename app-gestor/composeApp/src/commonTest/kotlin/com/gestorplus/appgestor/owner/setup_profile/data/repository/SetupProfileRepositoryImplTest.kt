package com.gestorplus.appgestor.owner.setup_profile.data.repository

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasource
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FakeSetupProfileRemoteDatasource : SetupProfileRemoteDatasource {
    var lastSavedData: String? = null
    override suspend fun saveWorkspaceProfile(uid: String, data: String) {
        lastSavedData = data
    }
}

class FakeFirebaseManagerForRepo : FirebaseManager() {
    var uploadCallCount = 0
    override suspend fun uploadImage(localPath: String): String {
        uploadCallCount++
        return "https://firebasestorage.com/uploaded_${localPath.substringAfterLast("/")}"
    }
    override fun getCurrentUserUid(): String? = "user_test_id"
}

class SetupProfileRepositoryImplTest {

    private val fakeRemoteDs = FakeSetupProfileRemoteDatasource()
    private val fakeFirebase = FakeFirebaseManagerForRepo()
    private val repository = SetupProfileRepositoryImpl(fakeRemoteDs, fakeFirebase)

    @Test
    fun `saveWorkspaceProfile uploads only local images`() = runTest {
        val profile = WorkspaceProfile(
            clinicName = "Test Clinic",
            fullName = "Test Doctor",
            specialities = emptyList(),
            biography = "",
            exactAddress = "",
            references = "",
            galleryImages = listOf(
                "content://media/photo1.jpg", // Local
                "https://already-online.com/photo2.png" // Remota
            )
        )

        val result = repository.saveWorkspaceProfile(profile)

        assertTrue(result.isSuccess)
        // Solo debió llamar a uploadImage una vez (por el content://)
        assertEquals(1, fakeFirebase.uploadCallCount)
        
        // Verificar que el JSON guardado contenga ambas URLs (la subida y la existente)
        assertTrue(fakeRemoteDs.lastSavedData?.contains("https://firebasestorage.com/uploaded_photo1.jpg") == true)
        assertTrue(fakeRemoteDs.lastSavedData?.contains("https://already-online.com/photo2.png") == true)
    }

    @Test
    fun `saveWorkspaceProfile fails if user is not authenticated`() = runTest {
        val anonymousFirebase = object : FakeFirebaseManagerForRepo() {
            override fun getCurrentUserUid(): String? = null
        }
        val repo = SetupProfileRepositoryImpl(fakeRemoteDs, anonymousFirebase)
        
        val profile = WorkspaceProfile(
            clinicName = "Any", fullName = "Any", specialities = emptyList(),
            biography = "", exactAddress = "", references = "", galleryImages = emptyList()
        )

        val result = repo.saveWorkspaceProfile(profile)

        assertTrue(result.isFailure)
        assertEquals("Usuario no autenticado", result.exceptionOrNull()?.message)
    }
}