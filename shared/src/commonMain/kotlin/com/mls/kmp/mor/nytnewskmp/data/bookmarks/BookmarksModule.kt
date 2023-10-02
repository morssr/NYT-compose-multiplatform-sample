package com.mls.kmp.mor.nytnewskmp.data.bookmarks

import com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache.BookmarksDao
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache.BookmarksDaoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val bookmarksModule = module {

    single<BookmarksDao> {
        BookmarksDaoImpl(
            dbInstance = get(),
            //TODO inject dispatcher
            coroutineContext = Dispatchers.IO,
            logger = get(),
        )
    }

    single<BookmarksRepository> {
        BookmarksRepositoryImpl(
            bookmarksDao = get(),
            logger = get(),
        )
    }
}