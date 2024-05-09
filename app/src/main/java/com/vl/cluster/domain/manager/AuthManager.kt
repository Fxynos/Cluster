package com.vl.cluster.domain.manager

import com.vl.cluster.domain.boundary.Network
import com.vl.cluster.domain.boundary.Session
import com.vl.cluster.domain.boundary.SessionStore

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