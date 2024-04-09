package com.vl.cluster.api.definition

import com.vl.cluster.api.definition.feature.Messenger
import com.vl.cluster.api.definition.feature.Newsfeed

interface Session {
    val sessionId: Int
    val sessionName: String
    val network: Network
    val newsfeed: Newsfeed
    val messenger: Messenger
}