package com.gestorplus.appgestor.core.firebase

/**
 * Define la promesa de que cada plataforma (Android/iOS) 
 * implementará su propia forma de obtener el token.
 */
expect suspend fun getToken(): String
