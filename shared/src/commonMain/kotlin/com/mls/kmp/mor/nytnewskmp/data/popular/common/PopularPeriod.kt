package com.mls.kmp.mor.nytnewskmp.data.popular.common

enum class PopularPeriod(private val period: String) {
    DAY("1"),
    WEEK("7"),
    MONTH("30");

    override fun toString() = period
}