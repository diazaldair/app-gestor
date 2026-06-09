package com.gestorplus.appgestor.core.persistence

import android.content.Context
import android.content.SharedPreferences

actual open class LocalPreferences(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("gestor_prefs", Context.MODE_PRIVATE)

    actual open fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    actual open fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    actual open fun getString(key: String, defaultValue: String?): String? {
        return prefs.getString(key, defaultValue)
    }

    actual open fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}
