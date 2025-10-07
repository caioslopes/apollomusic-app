package br.com.apollomusic.feature.owner.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.owner.home.presentation.OwnerHomeScreenViewModel
import br.com.apollomusic.feature.owner.home.ui.components.EstablishmentControl
import br.com.apollomusic.feature.owner.home.ui.components.EstablishmentDevices
import br.com.apollomusic.feature.owner.home.ui.components.PlaylistControl
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.ui.components.ApolloButton
import br.com.apollomusic.ui.components.ApolloCommonHeader
import br.com.apollomusic.ui.components.ApolloUserHeader
import br.com.apollomusic.ui.theme.Grey90

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerHomeScreen(
    navController: NavHostController,
    initialSpotifyCode: String? = null,
    viewModel: OwnerHomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedDevice by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getOwner()
        viewModel.getEstablishment()
        viewModel.getDevices()
        viewModel.getPlaylist()
    }

    LaunchedEffect(initialSpotifyCode) {
        if (!initialSpotifyCode.isNullOrBlank() && initialSpotifyCode != "{spotifyCode}") {
            viewModel.linkSpotify(initialSpotifyCode, navController)
        }
    }

    LaunchedEffect(uiState.establishment, uiState.devices) {
        val establishmentDeviceId = uiState.establishment?.deviceId
        val availableDevices = uiState.devices ?: emptyList()

        if (establishmentDeviceId != null &&
            availableDevices.any { it.id == establishmentDeviceId }
        ) {
            selectedDevice = establishmentDeviceId
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                ApolloCommonHeader(Modifier)
                ApolloUserHeader(
                    Modifier,
                    uiState.owner?.name ?: "",
                    onClickExit = { viewModel.onLogout(navController) },
                    uiState.owner?.hasThirdPartyAccess ?: false
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (uiState.owner?.hasThirdPartyAccess == true) {

                    EstablishmentControl(
                        name = uiState.establishment?.name ?: "",
                        statusText = if (uiState.establishment?.isOff == false) "Tocando agora" else "Desligado",
                        configured = uiState.establishment?.playlist != null,
                        enabled = uiState.establishment?.isOff == false,
                        onConfigClick = { viewModel.toggleEstablishment() }
                    )

                    EstablishmentDevices(
                        devices = uiState.devices ?: emptyList(),
                        selectedDeviceId = selectedDevice,
                        onSelectDevice = { deviceId ->
                            selectedDevice = deviceId
                            viewModel.setDevice(deviceId)
                        }
                    )

                    if (uiState.playlist == null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Grey90)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ApolloButton(
                                text = "Gerar Playlist",
                                onClick = { viewModel.createAndFetchPlaylist() },
                                icon = Icons.Default.Add,
                                iconPosition = "right"
                            )
                        }
                    } else {
                        PlaylistControl(
                            name = uiState.playlist?.name ?: "",
                            configured = (uiState.playlist?.initialArtists?.isNotEmpty() == true),
                            description = uiState.playlist?.description ?: "Lorem ipsum dolor sit amet.",
                            onConfigClick = {
                                navController.navigate(Screen.OwnerConfig.route)
                            }
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Grey90)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Conta do Spotify não vinculada ainda.")
                    }
                }
            }
        }
    }
}
