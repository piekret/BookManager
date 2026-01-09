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

    private var currentQuery: String = ""
    private var currentOffset: Int = 0
    private var currentPage: Int = 1
    private val limit: Int = 20
    private val loadedBooks = mutableListOf<Book>()
    private var isLoadingMore = false
    private var isEndReached = false

    init {
        loadInitial()
    }

    fun loadInitial() {
        _state.value = UiState.Loading
        currentOffset = 0
        currentPage = 1
        loadedBooks.clear()
        isEndReached = false
        loadData()
    }

    fun search(newQuery: String) {
        if (currentQuery == newQuery) return
        currentQuery = newQuery
        loadInitial()
    }

    fun loadMore() {
        if (isLoadingMore || isEndReached) return
        isLoadingMore = true
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val result = if (currentQuery.isBlank()) {
                repo.getFictionBooks(limit, currentOffset)
            } else {
                repo.searchBooks(currentQuery, currentPage, limit)
            }

            result.fold(
                onSuccess = { newBooks ->
                    isLoadingMore = false
                    if (newBooks.isEmpty()) {
                        isEndReached = true
                    } else {
                        loadedBooks.addAll(newBooks)
                        currentOffset += limit
                        currentPage += 1
                    }

                    if (loadedBooks.isEmpty()) {
                        _state.value = UiState.Error("No books found")
                    } else {
                        _state.value = UiState.Success(loadedBooks.toList())
                    }
                },
                onFailure = {
                    isLoadingMore = false
                    if (loadedBooks.isEmpty()) {
                        _state.value = UiState.Error("Something went wrong")
                    }
                }
            )
        }
    }
}