package com.example.bookmanager.data.model

data class Book(
    val workId: String,
    val title: String,
    val authors: String,
    val coverId: Int?,
    val firstPublishYear: Int?,
    val description: String? = null
)