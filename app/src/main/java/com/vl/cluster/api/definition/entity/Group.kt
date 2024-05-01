package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Network

data class Group(
    override val network: Network,
    override val profileId: Long,
    override val name: String,
    override val imageUrl: String?
): Profile