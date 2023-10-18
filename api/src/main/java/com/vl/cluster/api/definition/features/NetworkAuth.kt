package com.vl.cluster.api.definition.features

import com.vl.cluster.api.definition.ConnectionException
import com.vl.cluster.api.definition.Session

sealed interface NetworkAuth<S: Session> {
    enum class LoginType {
        PHONE,
        EMAIL,
        ID
    }

    val loginVariants: Set<LoginType>

    interface Password<S: Session>: NetworkAuth<S> {
        @Throws(WrongCredentialsException::class, ConnectionException::class)
        fun signIn(login: String, password: String): S

        class WrongCredentialsException: Exception()
    }

    interface Sms<S: Session>: NetworkAuth<S> // TODO

    interface Call<S: Session>: NetworkAuth<S> // TODO
}