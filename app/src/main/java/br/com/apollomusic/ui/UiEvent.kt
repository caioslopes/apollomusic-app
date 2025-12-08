package br.com.apollomusic.ui

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}