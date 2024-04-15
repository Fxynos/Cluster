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

    interface PasswordAuth: NetworkAuth {
        @Throws(
            WrongCredentialsException::class,
            ConnectionException::class,
            TwoFaException::class,
            CaptchaException::class,
            UnsupportedLoginMethodException::class
        )
        fun signIn(login: String, password: String): Session
    }

    interface CodeAuth: NetworkAuth {

        fun requestCode(login: String): CodeInfo

        @Throws(
            WrongCredentialsException::class,
            ConnectionException::class,
            CaptchaException::class,
            UnsupportedLoginMethodException::class
        )
        fun signIn(code: String): Session
    }

    /**
     * @param timeout seconds before code can be resent
     */
    data class CodeInfo(
        val codeLength: Int,
        val codeLocation: CodeLocation,
        val timeout: Int
    ) {
        companion object {
            @JvmStatic
            val CODE_LENGTH_UNDEFINED = -1
        }
    }

    enum class CodeLocation {
        SMS,
        APP,
        CALL,
        UNDEFINED
    }
}