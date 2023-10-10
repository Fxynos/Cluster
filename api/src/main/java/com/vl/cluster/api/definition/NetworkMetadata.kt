package com.vl.cluster.api.definition

interface NetworkMetadata {
    val name: String
    val id: Int
        get() = name.hashCode()
}