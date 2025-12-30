package com.example.bookmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.bookmanager.data.model.Book
import com.example.bookmanager.ui.common.UiState
import com.example.bookmanager.util.FavoriteManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritesViewModel(private val manager: FavoriteManager) : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Book>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Book>>> = _state

    fun load() { _state.value = UiState.Success(manager.getFavorites()) }

    init { load() }
}