package com.gestorplus.appgestor.core.persistence

import android.content.Context
import android.content.SharedPreferences

actual class LocalPreferences(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("gestor_prefs", Context.MODE_PRIVATE)

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    actual fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
}
