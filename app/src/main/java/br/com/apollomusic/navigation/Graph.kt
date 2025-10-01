package br.com.apollomusic.navigation

sealed class Graph(val route: String) {
    data object Root : Graph("root_graph")
    data object Owner : Graph("owner_graph")
    data object User : Graph("user_graph")
}