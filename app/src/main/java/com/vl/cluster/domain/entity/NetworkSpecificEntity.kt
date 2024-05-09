package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Network

interface NetworkSpecificEntity {
    val network: Network
}