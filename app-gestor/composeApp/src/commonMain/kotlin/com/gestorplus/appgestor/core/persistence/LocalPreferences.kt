package com.gestorplus.appgestor.core.persistence

expect class LocalPreferences {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
}
