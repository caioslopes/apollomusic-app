package br.com.apollomusic.feature.owner.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.apollomusic.domain.establishment.dto.device.Device
import br.com.apollomusic.domain.establishment.dto.device.DeviceType
import br.com.apollomusic.ui.theme.Grey90


@Composable
fun EstablishmentDevices(
    modifier: Modifier = Modifier,
    devices: List<Device>,
    selectedDeviceId: String?,
    onSelectDevice: (String) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Grey90)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(devices) { device ->
            DeviceOption(
                selected = selectedDeviceId == device.id,
                onClick = { onSelectDevice(device.id) },
                name = device.name,
                icon = getDeviceIcon(device.type)
            )
        }
    }
}


@Composable
fun getDeviceIcon(type: DeviceType): ImageVector {
    return when (type) {
        DeviceType.SMARTPHONE -> Icons.Default.Phone
        DeviceType.COMPUTER ->  Icons.Default.ExitToApp
        DeviceType.SPEAKER -> Icons.Default.Settings
    }
}
