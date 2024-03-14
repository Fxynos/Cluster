package com.vl.cluster.api.definition

import com.vl.cluster.api.definition.features.NetworkAuth
import com.vl.cluster.api.definition.features.NetworkMetadata

interface Network<S: Session>: NetworkMetadata {
    val authentication: NetworkAuth<S>
}