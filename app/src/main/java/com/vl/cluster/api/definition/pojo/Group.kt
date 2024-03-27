package com.vl.cluster.api.definition.pojo

import com.vl.cluster.api.definition.Network

data class Group(
    override val network: Network,
    override val id: Long,
    override val name: String,
    override val imageUrl: String?
): Profile