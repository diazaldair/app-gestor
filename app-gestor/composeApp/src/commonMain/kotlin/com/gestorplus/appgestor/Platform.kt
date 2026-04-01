package com.gestorplus.appgestor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform