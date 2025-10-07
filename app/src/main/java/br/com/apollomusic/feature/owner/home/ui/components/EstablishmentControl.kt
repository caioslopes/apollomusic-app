package br.com.apollomusic.feature.owner.home.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.apollomusic.ui.theme.Grey80
import br.com.apollomusic.ui.theme.Grey90
import br.com.apollomusic.ui.theme.Rose

@Composable
fun EstablishmentControl(
    modifier: Modifier = Modifier,
    name: String,
    statusText: String,
    configured: Boolean,
    enabled: Boolean,
    onConfigClick: (() -> Unit)? = null
) {
    val rowBackground = if (configured && enabled) Rose else Grey90
    val textColor = if (configured && enabled) Color.White else Rose
    val iconColor = if (configured && enabled) Color.White else Rose
    val chipBackground = if (configured) Color.White else Grey80
    val chipText = if (configured) statusText else "Não configurado"
    val chipTextColor = if (configured) Rose else Rose

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(rowBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Grey80)
            )

            Column {
                Text(
                    name,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = chipText,
                            fontSize = 12.sp,
                            color = chipTextColor,
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(0.dp, chipBackground),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = chipBackground,
                        labelColor = chipTextColor
                    ),
                )
            }
        }

        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Estabelecimento",
            modifier = Modifier
                .size(32.dp)
                .clickable() { onConfigClick?.invoke() },
            tint = iconColor
        )
    }
}

