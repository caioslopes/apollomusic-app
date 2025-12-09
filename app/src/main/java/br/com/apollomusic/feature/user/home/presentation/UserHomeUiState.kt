package br.com.apollomusic.feature.user.home.presentation

import android.net.Uri
import br.com.apollomusic.domain.establishment.dto.EstablishmentForUsersResponse
import br.com.apollomusic.domain.establishment.dto.Post
import br.com.apollomusic.domain.user.User

data class EstablishmentInfo(
    val name: String,
    //val imageUrl: String,
    val activeUsers: Int
)

data class UserHomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val user: User? = null,
    val establishmentInfo: EstablishmentForUsersResponse? = null,

    // Estados para o Feed
    val posts: List<Post> = emptyList(),
    val isLoadingFeed: Boolean = false,

    // Estados para o formulário de criação de post
    val newPostContent: String = "",
    val newPostImageUri: Uri? = null,
    val isCreatingPost: Boolean = false,
    val showCreatePostSheet: Boolean = false,
)
