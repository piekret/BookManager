package com.example.bookmanager.data.model

import com.squareup.moshi.Json

data class SubjectResponse(
    val works: List<WorkDto> = emptyList()
)

data class WorkDto(
    val key: String,
    val title: String? = null,
    val authors: List<AuthorDto>? = null,
    @Json(name = "cover_id") val coverId: Long? = null,
    @Json(name = "first_publish_year") val firstPublishYear: Int? = null
)

data class AuthorDto(
    val name: String? = null,
    val key: String? = null
)

data class WorkDetailDto(
    val title: String? = null,
    val description: Any? = null,
    @Json(name = "first_publish_date") val firstPublishDate: String? = null,
    val authors: List<AuthorRefDto>? = null,
    val covers: List<Long>? = null
)

data class AuthorRefDto(
    val author: AuthorDto? = null,
    val name: String? = null
)

data class SearchResponse(
    val docs: List<SearchDocDto> = emptyList()
)

data class SearchDocDto(
    val key: String,
    val title: String? = null,
    @Json(name = "author_name") val authorNames: List<String>? = null,
    @Json(name = "cover_i") val coverId: Long? = null,
    @Json(name = "first_publish_year") val firstPublishYear: Int? = null
)
