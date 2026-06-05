package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.core.data.repository.EventRepository
import com.gestorplus.appgestor.booking.data.mapper.FirebaseMapper
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseManager() }
    single { FirebaseMapper() }
    single { EventRepository(get()) }
}
