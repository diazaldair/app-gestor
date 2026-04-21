package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.data.local.getDatabaseBuilder
import org.koin.dsl.module

actual val platformModule = module {
    // Provee el builder de Room específico para iOS (no requiere parámetros)
    single { getDatabaseBuilder() }
}
