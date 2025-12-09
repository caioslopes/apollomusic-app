package br.com.apollomusic.feature.camera.presentation

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import java.io.File

data class CameraUiState(
    val isLoading: Boolean = true,
    val hasCameraPermission: Boolean = false,
    val capturedImageFile: File? = null,
    val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
)

sealed class CameraUiEvent {
    data class TakePhoto(val context: Context): CameraUiEvent()
    data class OnPermissionResult(val granted: Boolean) : CameraUiEvent()
    object SwitchCamera : CameraUiEvent()
    data class OnImageCaptured(val uri: Uri) : CameraUiEvent()
}