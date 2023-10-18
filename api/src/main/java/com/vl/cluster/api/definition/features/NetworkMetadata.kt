package com.vl.cluster.api.definition.features

interface NetworkMetadata {
    val name: String
    val id: Int
        get() = name.hashCode()
}