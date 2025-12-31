package com.example.bookmanager.ui.screens

import com.example.bookmanager.ui.viewmodels.BookDetailViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookmanager.ui.common.StateHost
import com.example.bookmanager.ui.common.coverUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    vm: BookDetailViewModel,
    onBack: () -> Unit
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book details") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            StateHost(state = state, onRetry = { vm.load() }) { ui ->
                AnimatedVisibility(visible = true, enter = fadeIn(tween(200))) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = coverUrl(ui.book.coverId, "L"),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Fit
                        )

                        Text(ui.book.title, style = MaterialTheme.typography.headlineSmall)
                        
                        Text(
                            text = "Author: ${ui.book.author}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        val publishDate = ui.book.publishDate ?: ui.book.publishYear?.toString()
                        if (publishDate != null) {
                            Text("Published: $publishDate", style = MaterialTheme.typography.bodySmall)
                        }

                        HorizontalDivider(Modifier.padding(vertical = 4.dp))

                        Text(
                            text = ui.book.description ?: "No description provided",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(16.dp))

                        FavoriteButton(
                            isFavorite = ui.isFavorite,
                            onClick = { vm.toggleFavorite() }
                        )
                    }
                }
            }
        }
    }
}
