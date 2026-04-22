package com.gestorplus.appgestor

import android.app.Application
import com.gestorplus.appgestor.di.appModule
import com.gestorplus.appgestor.di.databaseModule
import com.gestorplus.appgestor.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule, databaseModule, platformModule)
        }
    }
}
