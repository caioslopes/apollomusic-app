package br.com.apollomusic.feature.camera.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.apollomusic.ui.UiEvent.*
import br.com.apollomusic.ui.UiEvent as GlobalUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<GlobalUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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

    fun takePhoto(
        outputOptions: ImageCapture.OutputFileOptions,
        executor: Executor
    ) {
        val imageCapture = this.imageCapture ?: return

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    viewModelScope.launch {
                        _uiEvent.emit(GlobalUiEvent.ShowSnackbar("Foto salva com sucesso: ${outputFileResults.savedUri}"))
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    viewModelScope.launch {
                        _uiEvent.emit(GlobalUiEvent.ShowSnackbar("Erro ao salvar a foto: ${exception.message}"))
                    }
                }
            }
        )
    }
}
