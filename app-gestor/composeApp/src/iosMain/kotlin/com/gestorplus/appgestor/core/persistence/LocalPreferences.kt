package com.gestorplus.appgestor.core.persistence

import platform.Foundation.NSUserDefaults

actual class LocalPreferences {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        // If the key doesn't exist, boolForKey returns false by default.
        // We should check if the key exists to use the defaultValue.
        if (defaults.objectForKey(key) == null) {
            return defaultValue
        }
        return defaults.boolForKey(key)
    }

    actual fun putBoolean(key: String, value: Boolean) {
        defaults.setBool(value, forKey = key)
        defaults.synchronize()
    }
}
