package com.vl.cluster.presentation.entity

import androidx.annotation.DrawableRes

data class Post(
    val title: String,
    val datetime: String,
    val text: String,
    val profileImage: String?,
    @DrawableRes val networkIcon: Int?,
    val images: List<String> = emptyList()
)