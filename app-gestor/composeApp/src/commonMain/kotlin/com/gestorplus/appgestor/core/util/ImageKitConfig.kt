package com.gestorplus.appgestor.core.util

object ImageKitConfig {
    const val PUBLIC_KEY = "public_0azeJz+O/QI+dcY5TA7ediCi9e8="
    const val PRIVATE_KEY = "private_QUXE7jtptE7fYONHBGfgdCDUVJc="
    const val URL_ENDPOINT = "https://ik.imagekit.io/5pj4aajs5/"
    
    /**
     * Genera una URL optimizada de ImageKit.
     * @param path El path relativo, URL original o URI local
     * @param width Ancho deseado
     * @param height Alto deseado
     */
    fun getOptimizedUrl(path: String, width: Int? = null, height: Int? = null): String {
        if (path.isBlank()) return ""
        
        // Si es una URI local (Android/iOS), no podemos optimizarla con ImageKit
        if (path.startsWith("content://") || path.startsWith("file://") || path.startsWith("data:")) {
            return path
        }

        var baseUrl = path
        if (!path.startsWith("http")) {
            val endpoint = URL_ENDPOINT.removeSuffix("/")
            val cleanPath = if (path.startsWith("/")) path else "/$path"
            baseUrl = "$endpoint$cleanPath"
        }

        // Si no es una URL de ImageKit, no podemos aplicar transformaciones de ImageKit fácilmente
        if (!baseUrl.contains("ik.imagekit.io")) {
            return baseUrl
        }

        return applyTransformations(baseUrl, width, height)
    }

    private fun applyTransformations(url: String, width: Int?, height: Int?): String {
        if (width == null && height == null) return url
        
        val tr = mutableListOf<String>()
        width?.let { tr.add("w-$it") }
        height?.let { tr.add("h-$it") }
        
        val transformQuery = "tr=${tr.joinToString(",")}"
        
        return if (url.contains("?")) {
            "$url&$transformQuery"
        } else {
            // Intentar inserción en el path para mejor SEO/Cache
            if (url.contains(URL_ENDPOINT)) {
                 val parts = url.split(URL_ENDPOINT)
                 if (parts.size == 2) {
                     return "${URL_ENDPOINT}${transformQuery}/${parts[1]}"
                 }
            }
            "$url?$transformQuery"
        }
    }
}
