package com.vl.cluster.domain.boundary

import com.vl.cluster.domain.boundary.Session

/**
 * Persistent session store.
 * Network MUST update session store on authentication by itself.
 * @see Network
 */
interface SessionStore {
    fun getSessions(): Set<Session>
}