package com.vl.cluster.api.definition

/**
 * Persistent sessions store
 */
interface SessionStore {
    fun getSessions(): Set<Session>
    fun updateSessions(sessions: Set<Session>)
}