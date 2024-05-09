package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Network

data class Group(
    override val network: Network,
    override val profileId: Long,
    override val name: String,
    override val imageUrl: String?
): Profile