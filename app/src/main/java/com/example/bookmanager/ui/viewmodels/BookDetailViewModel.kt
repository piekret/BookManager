package com.example.bookmanager.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanager.data.model.Book
import com.example.bookmanager.data.repository.BookRepository
import com.example.bookmanager.ui.common.UiState
import com.example.bookmanager.util.FavoriteManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DetailUi(
    val book: Book,
    val isFavorite: Boolean
)

class BookDetailViewModel(
    private val repo: BookRepository,
    private val manager: FavoriteManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val workId: String = savedStateHandle["id"] ?: ""
    private val _state = MutableStateFlow<UiState<DetailUi>>(UiState.Loading)
    val state: StateFlow<UiState<DetailUi>> = _state

    fun load() {
        _state.value = UiState.Loading

        val base = manager.findById(workId) ?: Book(
            id = workId,
            title = "-",
            author = "-",
            coverId = null,
            publishYear = null,
            publishDate = null,
            description = null
        )

        viewModelScope.launch {
            val response = repo.detailBook(base)
            _state.value = response.fold(
                onSuccess = { b -> UiState.Success(DetailUi(b, manager.isFavorite(b.id))) },
                onFailure = { UiState.Error("Nie udało się pobrać szczegółów.") }
            )
        }
    }

    fun toggleFavorite() {
        val s = _state.value
        if (s !is UiState.Success) return
        val book = s.data.book
        if (manager.isFavorite(book.id)) {
            manager.removeFavorite(book.id)
        } else {
            manager.addFavorite(book)
        }
        _state.value = UiState.Success(s.data.copy(isFavorite = manager.isFavorite(book.id)))
    }

    init { if (workId.isNotBlank()) load() else _state.value = UiState.Error("No book Id") }
}