package com.mls.kmp.mor.nytnewskmp.core.data

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun create(): SqlDriver
}