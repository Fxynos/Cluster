package com.vl.cluster.domain.boundary

interface Network {
    val networkName: String
    val networkId: Int
        get() = networkName.hashCode()
    val authentication: NetworkAuth
    val sessionStore: SessionStore
}