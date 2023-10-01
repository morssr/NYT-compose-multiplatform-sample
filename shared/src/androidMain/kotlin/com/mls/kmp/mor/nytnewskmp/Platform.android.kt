package com.mls.kmp.mor.nytnewskmp

class AndroidPlatform : Platform {
    override val type: PlatformType = PlatformType.ANDROID
    override val version: String =  android.os.Build.VERSION.SDK_INT.toString()
}

actual fun getPlatform(): Platform = AndroidPlatform()