package com.vl.cluster.api.definition.feature

import com.vl.cluster.api.definition.exception.ConnectionException
import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.definition.exception.CaptchaException
import com.vl.cluster.api.definition.exception.TwoFaException
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException
import com.vl.cluster.api.definition.exception.WrongCredentialsException

sealed interface NetworkAuth {
    enum class LoginType {
        PHONE,
        EMAIL,
        ID
    }

    val loginVariants: Set<LoginType>

    interface Password: NetworkAuth {
        @Throws(
            WrongCredentialsException::class,
            ConnectionException::class,
            TwoFaException::class,
            CaptchaException::class,
            UnsupportedLoginMethodException::class
        )
        fun signIn(login: String, password: String): Session
    }

    interface CodeAuth {
        val nextRequestAvailableAt: Int

        /**
         * @return code length
         */
        fun requestCode(login: String): Int

        @Throws(
            WrongCredentialsException::class,
            ConnectionException::class,
            CaptchaException::class,
            UnsupportedLoginMethodException::class
        )
        fun signIn(code: String): Session
    }

    interface Sms: NetworkAuth, CodeAuth

    interface Call: NetworkAuth, CodeAuth

    /**
     * Authentication through session on trusted device
     */
    interface App: NetworkAuth, CodeAuth
}