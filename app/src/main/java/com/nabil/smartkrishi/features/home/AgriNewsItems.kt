package com.nabil.smartkrishi.features.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NewsItem(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val date: Int,
    )