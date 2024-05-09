package com.vl.cluster.presentation

import com.vl.cluster.R
import com.vl.cluster.domain.manager.NetworkReducer
import com.vl.cluster.domain.boundary.Network
import com.vl.cluster.data.network.vk.VkNetwork

object GlobalState { // TODO [tva] replace with managers and inject via DI
    lateinit var reducer: NetworkReducer

    fun Network.getIcon() = when(this) {
        is VkNetwork -> R.drawable.vk
        else -> 0
    }
}