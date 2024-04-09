package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Network

data class User(
    override val network: Network,
    override val id: Long,
    val firstname: String,
    val lastname: String,
    override val imageUrl: String?
): Profile {
    override val name: String
        get() = "$firstname $lastname"
}