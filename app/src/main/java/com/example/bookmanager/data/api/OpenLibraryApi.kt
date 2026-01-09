package com.example.bookmanager.data.api

import com.example.bookmanager.data.model.SubjectResponse
import com.example.bookmanager.data.model.WorkDetailDto
import com.example.bookmanager.data.model.AuthorDto
import com.example.bookmanager.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("subjects/fiction.json")
    suspend fun fiction(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): SubjectResponse

    @GET("search.json")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): SearchResponse

    @GET("works/{work_id}.json")
    suspend fun workDetail(@Path("work_id") workId: String): WorkDetailDto

    @GET("authors/{author_id}.json")
    suspend fun authorDetail(@Path("author_id") authorId: String): AuthorDto
}