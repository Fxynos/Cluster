package com.vl.cluster.api.definition.exception

import com.vl.cluster.api.definition.Session

class TwoFaException (
    val codeSource: CodeSource,
    private val onCodeConfirmed: Confirmation
) : Exception() {
    enum class CodeSource {
        SMS,
        APP
    }

    @Throws(ConnectionException::class, WrongCredentialsException::class)
    fun signIn(code: String) = onCodeConfirmed.signIn(code)

    fun interface Confirmation {
        @Throws(ConnectionException::class, WrongCredentialsException::class)
        fun signIn(code: String): Session
    }
}