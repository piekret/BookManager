package com.example.bookmanager.ui.common

fun coverUrl(coverId: Long?, size: String): String? {
    if (coverId == null) return null
    return "https://covers.openlibrary.org/b/id/${coverId}-${size}.jpg"
}
