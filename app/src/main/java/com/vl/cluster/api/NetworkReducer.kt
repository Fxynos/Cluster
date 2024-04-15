package com.vl.cluster.api

import com.vl.cluster.api.definition.exception.ConnectionException
import com.vl.cluster.api.definition.Network
import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.definition.exception.CaptchaException
import com.vl.cluster.api.definition.exception.TwoFaException
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException
import com.vl.cluster.api.definition.exception.WrongCredentialsException
import com.vl.cluster.api.definition.feature.NetworkAuth
import java.util.stream.Stream

class NetworkReducer(vararg val networks: Network) {
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

    fun findNetById(id: Int): Network = Stream.of(*networks)
        .filter { it.networkId == id }
        .findAny().get()
    fun findSessionById(id: Int): Session = Stream.of(*sessions.toTypedArray())
        .filter { it.sessionId == id }
        .findAny().get()
}