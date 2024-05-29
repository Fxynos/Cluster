package com.vl.cluster.domain.manager

import com.vl.cluster.domain.boundary.Network
import com.vl.cluster.domain.boundary.NetworkAuth
import com.vl.cluster.domain.boundary.Session
import com.vl.cluster.domain.boundary.SessionStore
import com.vl.cluster.domain.exception.CaptchaException
import com.vl.cluster.domain.exception.ConnectionException
import com.vl.cluster.domain.exception.TwoFaException
import com.vl.cluster.domain.exception.UnsupportedLoginMethodException
import com.vl.cluster.domain.exception.WrongCredentialsException
import okhttp3.internal.toImmutableList
import java.util.stream.Stream

class AuthManager(val networks: List<Network>) {

    private lateinit var cachedSessions: MutableList<Session>
    
    val isAuthorized: Boolean
        get() = cachedSessions.isNotEmpty()

    init { update() }

    fun update() {
        cachedSessions = networks
            .map(Network::sessionStore)
            .flatMap(SessionStore::getSessions)
            .toMutableList()
    }

    /* Network Reducer's guts */

    /**
     * @param nId network id
     * @return sessions of defined network or all sessions if *network id* is null
     */
    fun getSessions(nId: Int? = null): List<Session> = // exposes immutable list
        nId?.let { id -> cachedSessions.filter { session ->
            session.network.networkId == id
        } } ?: cachedSessions.toImmutableList()

    fun isPasswordAuthAvailable(nId: Int) = this.findNetById(nId).authentication is NetworkAuth.PasswordAuth
    fun isCodeAuthAvailable(nId: Int) = this.findNetById(nId).authentication is NetworkAuth.CodeAuth

    @Throws(
        WrongCredentialsException::class,
        ConnectionException::class,
        TwoFaException::class,
        CaptchaException::class,
        UnsupportedLoginMethodException::class
    )
    fun signInWithPassword(nId: Int, login: String, password: String): Session =
        try {
            (this.findNetById(nId).authentication as NetworkAuth.PasswordAuth)
                .signIn(login, password)
                .also { cachedSessions += it }
        } catch (e: CaptchaException) {
            throw CaptchaException(e.id, e.url) { key ->
                e.confirm(key).also { cachedSessions += it as Session }
            }
        }

    fun findNetById(id: Int): Network = networks.stream()
        .filter { it.networkId == id }
        .findAny().get()
    fun findSessionById(id: Int): Session = Stream.of(*cachedSessions.toTypedArray())
        .filter { it.sessionId == id }
        .findAny().get()
}