package com.vl.cluster.presentation.hilt

import com.vl.cluster.data.network.tg.TelegramNetwork
import com.vl.cluster.data.network.vk.VkNetwork
import com.vl.cluster.domain.boundary.Network

class NetworksWrapper(
    val vk: VkNetwork,
    val telegram: TelegramNetwork
) {
    val list: List<Network> = listOf(vk, telegram)
}