package br.com.apollomusic.ui.components

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
import br.com.apollomusic.domain.establishment.dto.artist.Artist
import coil.compose.AsyncImage

enum class SelectionMode {
    SINGLE,
    MULTI
}

/**
 * Um componente genérico e reutilizável para buscar e selecionar artistas.
 *
 * @param header Um slot Composable customizável para ser exibido no topo.
 * @param selectionMode Define o modo de seleção, se `SINGLE` (RadioButton) ou `MULTI` (Checkbox).
 * @param searchQuery O texto atual no campo de busca.
 * @param onQueryChange Lambda invocada quando o texto de busca é alterado.
 * @param onSearchClick Lambda invocada quando o ícone de busca é clicado.
 * @param searchResult A lista de artistas retornada pela busca para ser exibida.
 * @param selectedArtists A lista de artistas que estão atualmente selecionados.
 * @param onArtistSelect Lambda invocada quando um artista é selecionado ou desselecionado.
 * @param isSearching Um booleano que indica se uma busca está em andamento (para exibir o loading).
 */
@Composable
fun ApolloArtistSearch(
    header: @Composable () -> Unit,
    selectionMode: SelectionMode,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    searchResult: List<Artist>,
    selectedArtists: List<Artist>,
    onArtistSelect: (Artist) -> Unit,
    isSearching: Boolean
) {
    Column(modifier = Modifier.padding(16.dp)) {
        header()
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
                    val isSelected = selectedArtists.contains(artist)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onArtistSelect(artist) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (selectionMode) {
                            SelectionMode.MULTI -> {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { onArtistSelect(artist) }
                                )
                            }
                            SelectionMode.SINGLE -> {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onArtistSelect(artist) }
                                )
                            }
                        }

                        Spacer(Modifier.width(16.dp))
                        AsyncImage(
                            model = artist.images.firstOrNull(),
                            contentDescription = "Foto de ${artist.name}",
                            modifier = Modifier.size(48.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(artist.name)
                    }
                }
            }
        }
    }
}