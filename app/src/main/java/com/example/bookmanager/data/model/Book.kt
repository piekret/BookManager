package com.example.bookmanager.data.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val coverId: Long?,
    val publishYear: Int?,
    val publishDate: String?,
    val description: String? = null
)
