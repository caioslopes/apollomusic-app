package br.com.apollomusic.feature.user.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import br.com.apollomusic.ui.components.Skeleton
import br.com.apollomusic.ui.theme.Grey90

@Composable
fun EstablishmentInfoSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Grey90)
            .padding(16.dp)
    ) {
        Skeleton(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(28.dp), // Title size
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Skeleton(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp), // Subtitle size
            shape = RoundedCornerShape(8.dp)
        )
    }
}
