package com.gestorplus.appgestor.owner.setup_success.data.datasource.datasource

class SetupSuccessLocalDatasource {
    // En el futuro, esto puede leer de Room o DataStore
    suspend fun getClinicName(): String {
        return "BioMed" // Placeholder estático según el diseño
    }
}
