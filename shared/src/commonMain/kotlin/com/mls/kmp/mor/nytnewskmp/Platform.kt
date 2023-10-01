package com.mls.kmp.mor.nytnewskmp

interface Platform {
    val type: PlatformType
    val version: String
}

enum class PlatformType {
    ANDROID, IOS
}

expect fun getPlatform(): Platform