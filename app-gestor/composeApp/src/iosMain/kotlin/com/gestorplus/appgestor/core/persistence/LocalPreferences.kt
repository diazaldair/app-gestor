package com.gestorplus.appgestor.core.persistence

import platform.Foundation.NSUserDefaults

actual open class LocalPreferences {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual open fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        if (defaults.objectForKey(key) == null) {
            return defaultValue
        }
        return defaults.boolForKey(key)
    }

    actual open fun putBoolean(key: String, value: Boolean) {
        defaults.setBool(value, forKey = key)
        defaults.synchronize()
    }

    actual open fun getString(key: String, defaultValue: String?): String? {
        return defaults.stringForKey(key) ?: defaultValue
    }

    actual open fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
        defaults.synchronize()
    }
}
