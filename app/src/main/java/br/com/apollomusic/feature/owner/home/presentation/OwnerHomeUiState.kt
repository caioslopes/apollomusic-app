package br.com.apollomusic.feature.owner.home.presentation

import br.com.apollomusic.domain.establishment.dto.EstablishmentResponse
import br.com.apollomusic.domain.establishment.dto.device.Device
import br.com.apollomusic.domain.owner.dto.OwnerResponse

data class OwnerHomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val owner: OwnerResponse? = null,
    val establishment: EstablishmentResponse? = null,
    val devices: List<Device>? = null
)