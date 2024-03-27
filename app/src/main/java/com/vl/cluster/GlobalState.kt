package com.vl.cluster

import com.vl.cluster.api.NetworkReducer
import com.vl.cluster.api.definition.Network
import com.vl.cluster.api.network.vk.VkNetwork

object GlobalState { // TODO [tva] replace with managers and inject via DI
    val reducer: NetworkReducer = NetworkReducer(VkNetwork())

    fun Network.getIcon() = when(this) {
        is VkNetwork -> R.drawable.vk
        else -> 0
    }
}