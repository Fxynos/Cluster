package com.vl.cluster.domain.boundary

import androidx.annotation.DrawableRes

interface Network {
    val networkName: String
    val networkId: Int
    @get:DrawableRes val icon: Int
    val authentication: NetworkAuth
    val sessionStore: SessionStore
}