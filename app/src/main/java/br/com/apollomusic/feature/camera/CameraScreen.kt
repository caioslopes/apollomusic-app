package br.com.apollomusic.feature.camera

import android.Manifest
import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.camera.presentation.CameraScreenViewModel
import br.com.apollomusic.feature.camera.presentation.CameraUiEvent
import br.com.apollomusic.ui.UiEvent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.outlined.CameraAlt

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    viewModel: CameraScreenViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(key1 = viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(cameraPermissionState.status) {
        viewModel.onEvent(CameraUiEvent.OnPermissionResult(cameraPermissionState.status.isGranted))
    }

    LaunchedEffect(uiState.cameraSelector) {
        if (uiState.hasCameraPermission) {
            ProcessCameraProvider.getInstance(context).addListener({
                val cameraProvider = ProcessCameraProvider.getInstance(context).get()
            }, ContextCompat.getMainExecutor(context))
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.cameraUiEvent.collect { event ->
            when (event) {
                is CameraUiEvent.OnImageCaptured -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("captured_image_uri", event.uri)

                    navController.popBackStack()
                }

                is CameraUiEvent.OnPermissionResult -> TODO()
                CameraUiEvent.SwitchCamera -> TODO()
                is CameraUiEvent.TakePhoto -> TODO()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tirar Foto") },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.hasCameraPermission) {
                CameraPreview(
                    cameraSelector = uiState.cameraSelector,
                    onCameraReady = { previewView ->
                        ProcessCameraProvider.getInstance(context).addListener({
                            val cameraProvider = ProcessCameraProvider.getInstance(context).get()
                            viewModel.startCamera(cameraProvider, lifecycleOwner, previewView.surfaceProvider)
                        }, ContextCompat.getMainExecutor(context))
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.onEvent(CameraUiEvent.SwitchCamera) }) {
                        Icon(
                            imageVector = Icons.Default.Cameraswitch,
                            contentDescription = "Trocar Câmera",
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(CameraUiEvent.TakePhoto(context))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Tirar Foto"
                        )
                    }

                    Box(Modifier.fillMaxSize(0.12f))
                }


            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Permissão de câmera necessária para continuar.")
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun CameraPreview(
    cameraSelector: androidx.camera.core.CameraSelector,
    onCameraReady: (PreviewView) -> Unit
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(cameraSelector) {
        onCameraReady(previewView)
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    ) {
        onCameraReady(it)
    }
}

private fun getOutputDirectory(context: Context): File {
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, "ApolloMusic_Images").apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else context.filesDir
}