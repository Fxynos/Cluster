package com.vl.cluster.presentation.entity

import androidx.annotation.DrawableRes

data class Post(
    val id: String,
    val title: String,
    val datetime: String,
    val text: String,
    val profileImage: String?,
    @DrawableRes val networkIcon: Int?,
    val likesCount: Long,
    val commentsCount: Long,
    val repostsCount: Long,
    val hasLike: Boolean,
    val images: List<String> = emptyList()
)