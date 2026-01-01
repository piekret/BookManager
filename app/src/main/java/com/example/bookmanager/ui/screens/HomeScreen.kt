package com.example.bookmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookmanager.data.model.Book
import com.example.bookmanager.ui.common.StateHost
import com.example.bookmanager.ui.viewmodels.HomeViewModel
import com.example.bookmanager.ui.common.BookRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: () -> Unit
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BookManager") },
                actions = { 
                    IconButton(onClick = onThemeToggle) {
                        Text(text = if (isDarkTheme) "☀️" else "🌙")
                    }
                    IconButton(onClick = onFavoriteClick) { 
                        Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                    } 
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            StateHost(state = state, onRetry = { vm.load() }) { books ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(books, key = { it.id }) { book ->
                        BookRow(
                            title = book.title,
                            author = book.author,
                            coverId = book.coverId,
                            subtitle = book.publishYear?.let { "Year: $it" },
                            onClick = { onBookClick(book) }
                        )
                    }
                }
            }
        }
    }
}
