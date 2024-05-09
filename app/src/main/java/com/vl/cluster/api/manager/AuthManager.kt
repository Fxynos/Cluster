package com.vl.cluster.api.manager

import com.vl.cluster.api.definition.Network
import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.definition.SessionStore

class AuthManager(val networks: List<Network>) {

    private lateinit var cachedSessions: List<Session>
    
    val isAuthorized: Boolean
        get() = cachedSessions.isNotEmpty()

    init { update() }

    fun update() {
        cachedSessions = networks
            .map(Network::sessionStore)
            .flatMap(SessionStore::getSessions)
    }
}