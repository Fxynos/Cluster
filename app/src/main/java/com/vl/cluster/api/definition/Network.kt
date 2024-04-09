package com.vl.cluster.api.definition

import com.vl.cluster.api.definition.feature.NetworkAuth

interface Network {
    val networkName: String
    val networkId: Int
        get() = networkName.hashCode()
    val authentication: NetworkAuth
    val sessionStore: SessionStore
}