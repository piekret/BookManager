package com.example.bookmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmanager.data.model.Book
import com.example.bookmanager.data.repository.BookRepository
import com.example.bookmanager.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: BookRepository) : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Book>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Book>>> = _state

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            val response = repo.getFictionBooks()
            _state.value = response.fold(
                onSuccess = { if (it.isEmpty()) UiState.Error("No books found") else UiState.Success(it) },
                onFailure = { UiState.Error("Something went wrong") }
            )
        }
    }

    init { load() }
}