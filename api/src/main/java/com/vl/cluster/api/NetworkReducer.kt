package com.vl.cluster.api

import com.vl.cluster.api.definition.ConnectionException
import com.vl.cluster.api.definition.Network
import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.definition.features.NetworkAuth
import java.util.LinkedList
import java.util.stream.Stream

class NetworkReducer(val networks: Array<Network<out Session>>) {
    private val sessions = LinkedList<Session>()

    operator fun get(nId: Int) = require(nId)
    fun isPasswordAuthAvailable(nId: Int) = require(nId).authentication is NetworkAuth.Password
    fun isSmsAuthAvailable(nId: Int) = require(nId).authentication is NetworkAuth.Sms
    fun isCallAuthAvailable(nId: Int) = require(nId).authentication is NetworkAuth.Call
    @Throws(NetworkAuth.Password.WrongCredentialsException::class, ConnectionException::class)
    fun signInWithPassword(nId: Int, login: String, password: String) =
        (require(nId).authentication as NetworkAuth.Password)
            .signIn(login, password)
            .also { sessions += it }

    private fun require(id: Int) = findById(id)
        ?: throw NoSuchElementException("Network with \"$id\" id not found")
    private fun findById(id: Int): Network<out Session>? = Stream.of(*networks)
        .filter { it.id == id }
        .findAny().orElse(null)
}