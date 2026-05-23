package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.owner.home.data.datasource.HomeLocalDatasource
import com.gestorplus.appgestor.owner.home.data.repository.HomeRepositoryImpl
import com.gestorplus.appgestor.owner.home.domain.repository.HomeRepository
import com.gestorplus.appgestor.owner.home.domain.usecase.GetHomeDataUseCase
import com.gestorplus.appgestor.owner.home.presentation.viewmodel.OwnerHomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    // 1. Fuentes de Datos (Datasources)
    single { HomeLocalDatasource() }

    // 2. Repositorio
    single<HomeRepository> { HomeRepositoryImpl(get()) }

    // 3. Casos de Uso
    factoryOf(::GetHomeDataUseCase)

    // 4. ViewModel
    viewModelOf(::OwnerHomeViewModel)
}
