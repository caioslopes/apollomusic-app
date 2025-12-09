package br.com.apollomusic.feature.camera.presentation

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.apollomusic.BuildConfig
import br.com.apollomusic.ui.UiEvent.*
import br.com.apollomusic.ui.UiEvent as GlobalUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<GlobalUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _cameraUiEvent = MutableSharedFlow<CameraUiEvent>()
    val cameraUiEvent = _cameraUiEvent.asSharedFlow()

    private var imageCapture: ImageCapture? = null

    fun onEvent(event: CameraUiEvent) {
        when (event) {
            is CameraUiEvent.OnPermissionResult -> {
                _uiState.update { it.copy(hasCameraPermission = event.granted) }
                if (!event.granted) {
                    viewModelScope.launch {
                        _uiEvent.emit(ShowSnackbar("Permissão de câmera negada."))
                    }
                }
            }
            is CameraUiEvent.SwitchCamera -> {
                _uiState.update {
                    val newSelector = if (it.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                    it.copy(cameraSelector = newSelector)
                }
            }

            is CameraUiEvent.TakePhoto -> TODO()

            is CameraUiEvent.OnImageCaptured -> TODO()
        }
    }

    fun startCamera(
        cameraProvider: ProcessCameraProvider,
        lifecycleOwner: LifecycleOwner,
        surfaceProvider: Preview.SurfaceProvider
    ) {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().build()

        val cameraSelector = _uiState.value.cameraSelector

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            _uiState.update { it.copy(isLoading = false) }
        } catch (e: Exception) {
            viewModelScope.launch {
                _uiEvent.emit(GlobalUiEvent.ShowSnackbar("Falha ao iniciar a câmera: ${e.message}"))
            }
        }
    }

    private fun takePhoto(context: Context) {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            context.cacheDir,
            "post_image_${System.currentTimeMillis()}.jpg"
        )
        val outputUri = FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.provider",
            photoFile
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    viewModelScope.launch {
                        _cameraUiEvent.emit(CameraUiEvent.OnImageCaptured(output.savedUri ?: outputUri))
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    viewModelScope.launch {
                        _uiEvent.emit(GlobalUiEvent.ShowSnackbar("Erro ao salvar a foto: ${exc.message}"))
                    }
                }
            }
        )
    }
}
