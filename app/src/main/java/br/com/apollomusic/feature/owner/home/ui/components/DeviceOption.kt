package br.com.apollomusic.feature.owner.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.apollomusic.ui.theme.Rose

@Composable
fun DeviceOption(
    selected: Boolean,
    onClick: () -> Unit,
    name: String,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .size(width = 122.dp, height = 94.dp)
            .border(
                width = 1.dp,
                color = if (selected) Rose else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (selected) Rose.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = name,
                textAlign = TextAlign.Center,
                maxLines = 2,
                softWrap = true
            )
            Icon(
                imageVector = icon,
                contentDescription = "icone",
                modifier = Modifier.size(28.dp),
                tint = if (selected) Rose else Color.Black
            )
        }
    }
}


