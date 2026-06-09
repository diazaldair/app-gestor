package com.gestorplus.appgestor.core.locale

import java.util.Locale

actual fun getDeviceLanguage(): String {
    val lang = Locale.getDefault().language
    return if (lang in listOf("es", "en")) lang else "en"
}
