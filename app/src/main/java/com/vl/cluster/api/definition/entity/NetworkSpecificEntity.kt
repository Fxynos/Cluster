package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Network

interface NetworkSpecificEntity {
    val network: Network
}