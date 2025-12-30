package com.example.bookmanager.di

import com.example.bookmanager.data.api.RetrofitProvider
import com.example.bookmanager.data.repository.BookRepository
import com.example.bookmanager.util.FavoriteManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { RetrofitProvider.api }
    single { BookRepository(get()) }
    single { FavoriteManager(androidContext()) }
}
