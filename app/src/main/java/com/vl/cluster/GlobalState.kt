package com.vl.cluster

import androidx.annotation.DrawableRes
import com.vl.cluster.api.NetworkReducer
import com.vl.cluster.api.definition.Network
import com.vl.cluster.api.network.vk.VkNetwork

object GlobalState {
    val reducer: NetworkReducer = NetworkReducer(arrayOf(VkNetwork()))

    @DrawableRes
    fun Network<*>.getIcon() = when(this) {
        is VkNetwork -> R.drawable.vk
        else -> 0
    }
}