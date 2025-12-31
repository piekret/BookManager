package com.example.bookmanager.data.repository

import com.example.bookmanager.data.api.OpenLibraryApi
import com.example.bookmanager.data.model.*

class BookRepository(private val api: OpenLibraryApi) {
    private val bookCache = mutableMapOf<String, Book>()

    suspend fun getFictionBooks(limit: Int = 20): Result<List<Book>> = runCatching {
        val response = api.fiction(limit)
        val books = response.works.mapNotNull { dto ->
            val id = dto.key.removePrefix("/works/").trim()
            val title = dto.title ?: return@mapNotNull null
            val author = dto.authors?.firstOrNull()?.name ?: "Unnamed author"

            Book(
                id = id,
                title = title,
                author = author,
                coverId = dto.coverId,
                publishYear = dto.firstPublishYear,
                publishDate = null,
                description = null
            ).also { bookCache[it.id] = it }
        }
        books
    }

    private fun getDescription(dto: WorkDetailDto): String? {
        val desc = dto.description ?: return null
        return when (desc) {
            is String -> desc
            is Map<*, *> -> desc["value"]?.toString()
            else -> desc.toString()
        }
    }

    suspend fun detailBook(book: Book): Result<Book> = runCatching {
        val dto = api.workDetail(book.id)
        val cached = bookCache[book.id]

        val authorRef = dto.authors?.firstOrNull()
        var authorName = authorRef?.name ?: authorRef?.author?.name

        if (authorName == null && authorRef?.author?.key != null) {
            val key = authorRef.author.key
            val authorId = key.removePrefix("/authors/")
            val authDto = api.authorDetail(authorId)
            authorName = authDto.name
        }

        book.copy(
            title = dto.title ?: cached?.title ?: book.title,
            author = authorName ?: cached?.author ?: book.author,
            coverId = cached?.coverId ?: dto.covers?.firstOrNull() ?: book.coverId,
            publishDate = dto.firstPublishDate ?: book.publishDate,
            description = getDescription(dto)
        )
    }
}
