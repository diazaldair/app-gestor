package com.gestorplus.appgestor.core.persistence

import android.content.Context
import android.content.SharedPreferences

class AndroidLocalPreferences(private val context: Context) : LocalPreferences {
    private val prefs: SharedPreferences = context.getSharedPreferences("gestor_prefs", Context.MODE_PRIVATE)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return prefs.getString(key, defaultValue)
    }

    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}
