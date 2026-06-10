package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.SetupScheduleRemoteDatasource
import com.gestorplus.appgestor.owner.setup_schedule.data.repository.SetupScheduleRepositoryImpl
import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.SetupScheduleService
import com.gestorplus.appgestor.owner.setup_schedule.data.datasource.mapper.ShiftMapper
import com.gestorplus.appgestor.owner.setup_schedule.domain.repository.SetupScheduleRepository
import com.gestorplus.appgestor.owner.setup_schedule.domain.usecase.SaveWorkspaceScheduleUseCase
import com.gestorplus.appgestor.owner.setup_schedule.presentation.viewmodel.WorkspaceSetupScheduleViewModel
import com.gestorplus.appgestor.owner.working_hours.presentation.viewmodel.WorkingHoursViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val setupScheduleModule = module {
    // 1. Servicios y Mappers
    single { SetupScheduleService(get()) }
    single { ShiftMapper() }

    // 2. Fuentes de Datos (Datasources)
    single { SetupScheduleRemoteDatasource(get()) }

    // 3. Repositorio con inyección explícita para evitar errores de compilación
    single<SetupScheduleRepository> { 
        SetupScheduleRepositoryImpl(
            remoteDatasource = get(),
            firebaseManager = get(),
            shiftDao = get(),
            mapper = get(),
            setupScheduleService = get()
        ) 
    }

    // 4. Casos de Uso
    factoryOf(::SaveWorkspaceScheduleUseCase)

    // 5. ViewModel
    viewModelOf(::WorkspaceSetupScheduleViewModel)
    viewModelOf(::WorkingHoursViewModel)
}
