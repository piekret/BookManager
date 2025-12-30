package com.example.bookmanager.data.repository

import com.example.bookmanager.data.api.OpenLibraryApi
import com.example.bookmanager.data.model.Book
import com.example.bookmanager.data.model.WorkDetailDto

class BookRepository(private val api: OpenLibraryApi) {
    suspend fun getFictionBooks(limit: Int = 20): Result<List<Book>> = runCatching {
        val response = api.fiction(limit)
        response.works.mapNotNull { dto ->
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
            )
        }
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
        book.copy(
            title = dto.title ?: book.title,
            publishDate = dto.firstPublishDate ?: book.publishDate,
            description = getDescription(dto)
        )
    }
}