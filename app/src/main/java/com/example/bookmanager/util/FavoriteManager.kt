package com.example.bookmanager.util

import android.content.Context
import android.net.Uri
import com.example.bookmanager.data.model.Book
import androidx.core.content.edit

class FavoriteManager(context: Context) {
    private val preferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val key = "favorite_books"

    fun getFavorites(): List<Book> {
        return (preferences.getStringSet(key, emptySet()) ?: emptySet())
            .mapNotNull { decode(it) }
            .sortedBy { it.title.lowercase() }
    }

    fun isFavorite(id: String): Boolean {
        return preferences.getStringSet(key, emptySet())?.any { it.startsWith("$id|") } ?: false
    }

    fun addFavorite(book: Book) {
        val curr = preferences.getStringSet(key, emptySet())?.toMutableSet() ?: mutableSetOf()
        curr.removeAll { it.startsWith("${book.id}|") }
        curr.add(encode(book))
        preferences.edit { putStringSet(key, curr) }
    }

    fun removeFavorite(id: String) {
        val curr = preferences.getStringSet(key, emptySet())?.toMutableSet() ?: mutableSetOf()
        curr.removeAll { it.startsWith("$id|") }
        preferences.edit { putStringSet(key, curr) }
    }

    fun findById(id: String): Book? {
        return (preferences.getStringSet(key, emptySet()) ?: emptySet())
            .firstOrNull { it.startsWith("$id|") }
            ?.let { decode(it) }
    }

    private fun encode(book: Book): String {
        val title = Uri.encode(book.title)
        val author = Uri.encode(book.author)
        val coverIdStr = book.coverId?.toString() ?: "null"
        val yearStr = book.publishYear?.toString() ?: "null"

        return "${book.id}|$title|$author|$coverIdStr|$yearStr"
    }

    private fun decode(encoded: String): Book? {
        val parts = encoded.split("|")
        if (parts.size < 5) return null

        val id = parts[0]
        val title = Uri.decode(parts[1])
        val author = Uri.decode(parts[2])
        val coverId = if (parts[3] == "null") null else parts[3].toLongOrNull()
        val year = if (parts[4] == "null") null else parts[4].toIntOrNull()

        return Book(
            id = id,
            title = title,
            author = author,
            coverId = coverId,
            publishYear = year,
            publishDate = null,
            description = null
        )
    }
}
