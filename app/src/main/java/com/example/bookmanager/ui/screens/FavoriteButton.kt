package com.example.bookmanager.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun FavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    FilledTonalButton(onClick = onClick) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null
        )
        AnimatedContent(
            targetState = isFavorite,
            label = "favLabel",
            transitionSpec = {
                (fadeIn(tween(160)) togetherWith fadeOut(tween(160)))
                    .using(SizeTransform(clip = false))
            }
        ) { fav ->
            Text(if (fav) "Remove from favorites" else "Add to favorites")
        }
    }
}
