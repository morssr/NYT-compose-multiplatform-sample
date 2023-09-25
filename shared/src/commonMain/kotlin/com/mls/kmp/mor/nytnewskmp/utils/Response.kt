package com.mls.kmp.mor.nytnewskmp.utils

sealed class Response<out T> {
    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Failure<out T>(
        val error: Exception,
        val fallbackData: T? = null
        ): Response<T>()

    object Loading: Response<Nothing>()
}