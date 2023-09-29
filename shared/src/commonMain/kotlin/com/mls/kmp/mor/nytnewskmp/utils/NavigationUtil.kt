package com.mls.kmp.mor.nytnewskmp.utils

import cafe.adriel.voyager.navigator.Navigator

fun Navigator.findRouteNavigator(): Navigator {
    return if (parent == null) {
        this
    } else {
        parent!!.findRouteNavigator()
    }
}