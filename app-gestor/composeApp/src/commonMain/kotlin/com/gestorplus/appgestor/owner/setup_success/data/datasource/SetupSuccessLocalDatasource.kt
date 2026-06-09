package com.gestorplus.appgestor.owner.setup_success.data.datasource

import com.gestorplus.appgestor.core.persistence.LocalPreferences

class SetupSuccessLocalDatasource(private val prefs: LocalPreferences) {
    suspend fun getClinicName(): String {
        return prefs.getString("registered_clinic_name", "Mi Clínica") ?: "Mi Clínica"
    }
}
