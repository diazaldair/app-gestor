package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.core.data.local.dao.*
import com.gestorplus.appgestor.owner.schedule.data.datasource.ScheduleRemoteDatasource
import com.gestorplus.appgestor.owner.schedule.data.datasource.service.ScheduleRemoteService
import com.gestorplus.appgestor.owner.schedule.data.mapper.ScheduleMapper
import com.gestorplus.appgestor.owner.schedule.data.repository.ScheduleRepositoryImpl
import com.gestorplus.appgestor.owner.schedule.domain.repository.ScheduleRepository
import com.gestorplus.appgestor.owner.schedule.domain.usecase.*
import com.gestorplus.appgestor.owner.working_hours.presentation.viewmodel.WorkingHoursViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val scheduleModule = module {
    // Services
    single { ScheduleRemoteService(get()) }
    // Datasources
    single { ScheduleRemoteDatasource(get()) }
    // Mapper
    single { ScheduleMapper() }
    // Repositorio
    single<ScheduleRepository> {
        ScheduleRepositoryImpl(
            shiftDao = get(),
            masterDao = get(),
            lunchDao = get(),
            timingDao = get(),
            exceptionDao = get(),
            remoteDs = get(),
            mapper = get(),
            uidProvider = { get<com.gestorplus.appgestor.data.datasource.FirebaseManager>().getCurrentUserUid() }
        )
    }
    // Use Cases
    factoryOf(::GetWorkingShiftsUseCase)
    factoryOf(::SaveWorkingShiftsUseCase)
    factoryOf(::GetMasterScheduleUseCase)
    factoryOf(::SaveMasterScheduleUseCase)
    factoryOf(::GetAutoLunchUseCase)
    factoryOf(::SaveAutoLunchUseCase)
    factoryOf(::GetTimingDefaultsUseCase)
    factoryOf(::SaveTimingDefaultsUseCase)
    factoryOf(::GetExceptionsUseCase)
    factoryOf(::SaveExceptionUseCase)
    factoryOf(::DeleteExceptionUseCase)

    // ViewModels
    viewModelOf(::WorkingHoursViewModel)
}