package com.gestorplus.appgestor.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

actual class ImagePicker {
    actual fun pickImage() {
        println("ImagePicker: iOS no implementado aún")
    }
}

@Composable
actual fun rememberImagePicker(onImagePicked: (String) -> Unit): ImagePicker {
    return remember { ImagePicker() }
}
