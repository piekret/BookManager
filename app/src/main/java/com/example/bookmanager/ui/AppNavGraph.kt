package com.example.bookmanager.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookmanager.ui.viewmodels.*
import com.example.bookmanager.ui.screens.*
import com.example.bookmanager.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { fadeIn(tween(300)) },
        exitTransition = { fadeOut(tween(300)) }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                vm = koinViewModel(),
                onBookClick = { book ->
                    navController.navigate(Screen.Detail.createRoute(book.id))
                },
                onFavoriteClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                vm = koinViewModel(),
                onBookClick = { book ->
                    navController.navigate(Screen.Detail.createRoute(book.id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) {
            BookDetailScreen(
                vm = koinViewModel(),
                onBack = { navController.popBackStack() }
            )
        }
    }
}
