package br.com.apollomusic.feature.user.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.domain.establishment.dto.Post
import br.com.apollomusic.feature.user.home.presentation.UserHomeScreenViewModel
import br.com.apollomusic.feature.user.home.ui.components.EstablishmentInfoCard
import br.com.apollomusic.feature.user.home.ui.components.EstablishmentInfoSkeleton
import br.com.apollomusic.ui.components.ApolloCommonHeader
import br.com.apollomusic.ui.components.ApolloUserHeader
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.util.Objects
import br.com.apollomusic.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    navController: NavHostController,
    viewModel: UserHomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.onImageCaptured(uiState.newPostImageUri)
        } else {
            viewModel.onImageCaptured(null)
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                ApolloCommonHeader(Modifier)
                ApolloUserHeader(
                    modifier = Modifier,
                    userName = uiState.user?.username ?: "",
                    onClickExit = { viewModel.onLogout(navController) },
                    hasThirdPartyAccess = false,
                    isLoading = uiState.isLoading,
                    showSpotifySection = false
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onShowCreatePostSheet(true) }) {
                Icon(Icons.Default.Add, contentDescription = "Criar Post")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (uiState.isLoading) {
                    EstablishmentInfoSkeleton()
                } else if (uiState.establishmentInfo != null) {
                    EstablishmentInfoCard(info = uiState.establishmentInfo!!)
                } else if (uiState.errorMessage != null) {
                    Text(text = "Erro ao carregar: ${uiState.errorMessage}")
                }
            }

            item {
                Text(
                    text = "Feed de Novidades",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            if (uiState.isLoadingFeed) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(uiState.posts) { post ->
                    PostCard(post = post)
                }
            }
        }

        if (uiState.showCreatePostSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onShowCreatePostSheet(false) },
                sheetState = sheetState
            ) {
                CreatePostForm(
                    content = uiState.newPostContent,
                    onContentChange = viewModel::onContentChange,
                    imageUri = uiState.newPostImageUri,
                    onTakePhotoClick = {
                        val file = File(context.cacheDir, "post_image_${System.currentTimeMillis()}.jpg")
                        val uri = FileProvider.getUriForFile(
                            Objects.requireNonNull(context),
                            BuildConfig.APPLICATION_ID + ".provider", file
                        )
                        viewModel.onImageCaptured(uri)
                        cameraLauncher.launch(uri)
                    },
                    onSubmitClick = { viewModel.createPost(context) },
                    isCreatingPost = uiState.isCreatingPost
                )
            }
        }
    }
}

@Composable
fun PostCard(post: Post) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            if (post.imageUrls.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(post.imageUrls.first()),
                    contentDescription = "Imagem do post de ${post.username}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(Modifier.padding(16.dp)) {
                Text(text = post.username, style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Text(text = post.content, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun CreatePostForm(
    content: String,
    onContentChange: (String) -> Unit,
    imageUri: Uri?,
    onTakePhotoClick: () -> Unit,
    onSubmitClick: () -> Unit,
    isCreatingPost: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Novo Post", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("O que está acontecendo?") },
            minLines = 3
        )

        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Imagem para o post",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onTakePhotoClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Tirar Foto")
            }

            Button(
                onClick = onSubmitClick,
                enabled = !isCreatingPost && imageUri != null,
                modifier = Modifier.weight(1f)
            ) {
                if (isCreatingPost) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Publicar")
                }
            }
        }
    }
}
