package com.vl.cluster.domain.manager

import com.vl.cluster.domain.exception.ConnectionException
import com.vl.cluster.domain.boundary.Network
import com.vl.cluster.domain.boundary.Session
import com.vl.cluster.domain.exception.CaptchaException
import com.vl.cluster.domain.exception.TwoFaException
import com.vl.cluster.domain.exception.UnsupportedLoginMethodException
import com.vl.cluster.domain.exception.WrongCredentialsException
import com.vl.cluster.domain.boundary.NetworkAuth
import java.util.stream.Stream

class NetworkReducer(val networks: List<Network>) {

    private val sessions = ArrayList<Session>()

    /**
     * @param nId network id
     * @return sessions of defined network or all sessions if *network id* is null
     */
    fun getSessions(nId: Int? = null): List<Session> = // exposes immutable list
        nId?.let { id -> sessions.filter { session -> session.network.networkId == id } } ?: sessions
    //fun isPasswordAuthAvailable(nId: Int) = this.findNetById(nId).authentication is NetworkAuth.PasswordAuth
    //fun isSmsAuthAvailable(nId: Int) = this.findNetById(nId).authentication is NetworkAuth.Sms
    //fun isCallAuthAvailable(nId: Int) = this.findNetById(nId).authentication is NetworkAuth.Call
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
                .also { sessions += it }
        } catch (e: CaptchaException) {
            throw CaptchaException(e.id, e.url) { key ->
                e.confirm(key).also { sessions += it as Session }
            }
        }

    fun findNetById(id: Int): Network = networks.stream()
        .filter { it.networkId == id }
        .findAny().get()
    fun findSessionById(id: Int): Session = Stream.of(*sessions.toTypedArray())
        .filter { it.sessionId == id }
        .findAny().get()
}