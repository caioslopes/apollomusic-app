package br.com.apollomusic.ui.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApolloUserHeader(modifier: Modifier, userName: String, onClickExit: () -> Unit, hasThirdPartyAccess: Boolean = false ) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Text(
                "Perfil",
                modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Column {
                    Text(
                        "Informações da conta",
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = userName)

                    }

                }

                Column {
                    Text(
                        "Spotify",
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable {
                             if(!hasThirdPartyAccess) {
                                 val intent = Intent(Intent.ACTION_VIEW,
                                     "https://accounts.spotify.com/pt-BR/authorize?response_type=code&scope=user-read-playback-state%20user-modify-playback-state%20user-read-currently-playing%20playlist-read-private%20playlist-read-collaborative%20playlist-modify-private%20playlist-modify-public&client_id=d5efeedcdd7f41f8a323384fcd81f2be&redirect_uri=apollomusic://callback".toUri())
                                 context.startActivity(intent)
                             }
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                       if(!hasThirdPartyAccess) {
                           Text("Vincular uma conta")
                           Icon(
                               imageVector = Icons.Default.Settings,
                               contentDescription = "Editar usuário",
                               modifier = Modifier.size(24.dp)
                           )
                       } else {
                           Text("Conta vinculada!")
                       }
                    }

                }

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable {
                            onClickExit()
                            showBottomSheet = false
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Sair", color = Color.Red)
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Sair",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Red
                    )
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable {
                scope.launch {
                    showBottomSheet = true
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = userName, fontWeight = FontWeight.Bold)
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "usuário",
            modifier = Modifier.size(24.dp)
        )
    }
}
