package com.example.bookmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookmanager.data.model.Book
import com.example.bookmanager.ui.common.BookRow
import com.example.bookmanager.ui.common.StateHost
import com.example.bookmanager.ui.viewmodels.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    vm: FavoritesViewModel,
    onBookClick: (Book) -> Unit,
    onBack: () -> Unit
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.load()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            StateHost(state = state, onRetry = { vm.load() }) { books ->
                if (books.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No favorites")
                    }
                } else {
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
}
