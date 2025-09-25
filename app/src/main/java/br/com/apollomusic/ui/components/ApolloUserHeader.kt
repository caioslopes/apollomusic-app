package br.com.apollomusic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApolloUserHeader(modifier: Modifier, userName: String, onClickExit: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

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
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar usuário",
                            modifier = Modifier.size(24.dp)
                        )
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
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Vincular uma conta")
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Editar usuário",
                            modifier = Modifier.size(24.dp)
                        )
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
