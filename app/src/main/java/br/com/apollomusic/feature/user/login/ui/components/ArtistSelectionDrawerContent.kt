package br.com.apollomusic.feature.user.login.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import br.com.apollomusic.domain.establishment.dto.Artist
import br.com.apollomusic.ui.components.ApolloInputText
import coil.compose.AsyncImage

@Composable
fun ArtistSelectionDrawerContent(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    searchResult: List<Artist>,
    selectedArtists: List<Artist>,
    onArtistSelect: (Artist) -> Unit,
    isSearching: Boolean
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Selecione até 3 artistas", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        ApolloInputText(
            value = searchQuery,
            onValueChange = onQueryChange,
            label = "Nome do artista",
            trailingIcon = {
                IconButton(onClick = onSearchClick, enabled = !isSearching) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        if (isSearching) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(searchResult) { artist ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onArtistSelect(artist) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedArtists.contains(artist),
                            onCheckedChange = { onArtistSelect(artist) }
                        )
                        Spacer(Modifier.width(16.dp))
                        AsyncImage(
                            model = artist.images.firstOrNull(),
                            contentDescription = "Foto de ${artist.name}",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            //placeholder = painterResource(id = R.drawable.ic_placeholder_artist),
                            //error = painterResource(id = R.drawable.ic_placeholder_artist)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(artist.name)
                    }
                }
            }
        }
    }
}