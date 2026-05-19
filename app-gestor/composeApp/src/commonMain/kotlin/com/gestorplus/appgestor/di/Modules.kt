package com.gestorplus.appgestor.di

import org.koin.core.module.Module

/**
 * Platform specific module expectation.
 * Implementation found in androidMain and iosMain.
 */
expect val platformModule: Module
