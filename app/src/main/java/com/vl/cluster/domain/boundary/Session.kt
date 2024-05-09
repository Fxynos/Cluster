package com.vl.cluster.domain.boundary

import java.util.Objects

abstract class Session: Newsfeed, Messenger {
    abstract val sessionId: Int
    abstract val sessionName: String
    abstract val network: Network
    abstract val newsfeed: Newsfeed
    abstract val messenger: Messenger

    override fun equals(other: Any?) = other
        ?.let { it as? Session }
        ?.let { it.sessionId == sessionId && it.network == network } == true

    override fun hashCode(): Int = Objects.hash(network, sessionId)
}