package com.vl.cluster.presentation.entity

import androidx.annotation.DrawableRes

data class NetworkData(
    val name: String,
    val id: Int,
    @DrawableRes val icon: Int
)