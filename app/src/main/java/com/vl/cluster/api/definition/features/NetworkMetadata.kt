package com.vl.cluster.api.definition.features

interface NetworkMetadata {
    val networkName: String
    val networkId: Int
        get() = networkName.hashCode()
}