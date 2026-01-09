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
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction

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
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleItemIndex >= totalItems - 2
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            vm.loadMore()
        }
    }

    Scaffold(
        topBar = {
            Column {
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
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search books...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        vm.search(searchQuery)
                    }),
                    singleLine = true
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            StateHost(state = state, onRetry = { vm.loadInitial() }) { books ->
                LazyColumn(
                    state = listState,
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
