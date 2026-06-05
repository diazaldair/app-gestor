package com.gestorplus.appgestor.util

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

actual class ImagePicker(
    private val launcher: ActivityResultLauncher<PickVisualMediaRequest>
) {
    actual fun pickImage() {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

@Composable
actual fun rememberImagePicker(onImagePicked: (String) -> Unit): ImagePicker {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { onImagePicked(it.toString()) }
    }
    return remember { ImagePicker(launcher) }
}
