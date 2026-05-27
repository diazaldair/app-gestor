package com.gestorplus.appgestor.core.locale

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getDeviceLanguage(): String {
    val lang = NSLocale.currentLocale.languageCode ?: "en"
    return if (lang in listOf("es", "en", "fr")) lang else "en"
}
