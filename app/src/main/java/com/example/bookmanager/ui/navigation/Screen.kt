package com.example.bookmanager.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object Detail : Screen("detail/{bookId}") {
        fun createRoute(bookId: String) = "detail/$bookId"
    }
}
