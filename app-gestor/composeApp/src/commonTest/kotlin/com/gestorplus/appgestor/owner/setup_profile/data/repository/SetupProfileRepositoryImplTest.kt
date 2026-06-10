package com.gestorplus.appgestor.owner.setup_profile.data.repository

import com.gestorplus.appgestor.core.data.datasource.FirebaseManager
import com.gestorplus.appgestor.owner.setup_profile.domain.model.WorkspaceProfile
import com.gestorplus.appgestor.owner.setup_profile.data.datasource.SetupProfileRemoteDatasource
import com.gestorplus.appgestor.core.persistence.LocalPreferences
import com.gestorplus.appgestor.clinicProfile.data.local.dao.ClinicProfileDao
import com.gestorplus.appgestor.clinicProfile.data.local.entity.ClinicProfileEntity
import kotlinx.coroutines.flow.Flow
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

open class FakeFirebaseManagerForRepo : FirebaseManager() {
    var uploadCallCount = 0
    override suspend fun uploadImage(localPath: String): String {
        uploadCallCount++
        return "https://firebasestorage.com/uploaded_${localPath.substringAfterLast("/")}"
    }
    override fun getCurrentUserUid(): String? = "user_test_id"
}

class FakeLocalPreferences : LocalPreferences() {
    private val data = mutableMapOf<String, String>()
    override fun putString(key: String, value: String) { data[key] = value }
    override fun getString(key: String, defaultValue: String?): String? = data[key] ?: defaultValue
}

class FakeClinicProfileDao : ClinicProfileDao {
    override fun getProfile(): Flow<ClinicProfileEntity?> = error("Not implemented")
    override suspend fun insertProfile(profile: ClinicProfileEntity) {}
    override suspend fun clearProfile() {}
}

class SetupProfileRepositoryImplTest {

    private val fakeRemoteDs = FakeSetupProfileRemoteDatasource()
    private val fakeFirebase = FakeFirebaseManagerForRepo()
    private val fakeLocalPrefs = FakeLocalPreferences()
    private val fakeDao = FakeClinicProfileDao()
    private val repository = SetupProfileRepositoryImpl(fakeRemoteDs, fakeFirebase, fakeLocalPrefs, fakeDao)

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
        assertEquals(1, fakeFirebase.uploadCallCount)
        
        assertTrue(fakeRemoteDs.lastSavedData?.contains("https://firebasestorage.com/uploaded_photo1.jpg") == true)
        assertTrue(fakeRemoteDs.lastSavedData?.contains("https://already-online.com/photo2.png") == true)
    }

    @Test
    fun `saveWorkspaceProfile fails if user is not authenticated`() = runTest {
        val anonymousFirebase = object : FakeFirebaseManagerForRepo() {
            override fun getCurrentUserUid(): String? = null
        }
        val repo = SetupProfileRepositoryImpl(fakeRemoteDs, anonymousFirebase, fakeLocalPrefs, fakeDao)
        
        val profile = WorkspaceProfile(
            clinicName = "Any", fullName = "Any", specialities = emptyList(),
            biography = "", exactAddress = "", references = "", galleryImages = emptyList()
        )

        val result = repo.saveWorkspaceProfile(profile)

        assertTrue(result.isFailure)
        assertEquals("Usuario no autenticado", result.exceptionOrNull()?.message)
    }
}
