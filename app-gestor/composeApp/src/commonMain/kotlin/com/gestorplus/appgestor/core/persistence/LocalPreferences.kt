package com.gestorplus.appgestor.core.persistence

expect open class LocalPreferences {
    open fun getBoolean(key: String, defaultValue: Boolean): Boolean
    open fun putBoolean(key: String, value: Boolean)
}
