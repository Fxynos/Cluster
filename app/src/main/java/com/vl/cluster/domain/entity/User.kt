package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Network

data class User(
    override val network: Network,
    override val profileId: String,
    val firstname: String,
    val lastname: String,
    override val imageUrl: String?
): Profile {
    override val name: String
        get() = "$firstname $lastname"
}