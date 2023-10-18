package com.vl.cluster.api.definition

import com.vl.cluster.api.definition.features.NetworkMetadata
import com.vl.cluster.api.definition.features.Newsfeed

abstract class Session(network: Network<*>): NetworkMetadata by network, Newsfeed