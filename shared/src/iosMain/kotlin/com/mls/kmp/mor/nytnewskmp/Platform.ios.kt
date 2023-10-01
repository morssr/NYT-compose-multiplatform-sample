package com.mls.kmp.mor.nytnewskmp

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val type: PlatformType = PlatformType.IOS
    override val version: String = UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()