package com.vl.cluster.domain.boundary

import com.vl.cluster.domain.entity.CodeLocation
import com.vl.cluster.domain.entity.LoginType
import com.vl.cluster.domain.exception.ConnectionException
import com.vl.cluster.domain.exception.CaptchaException
import com.vl.cluster.domain.exception.TwoFaException
import com.vl.cluster.domain.exception.UnsupportedLoginMethodException
import com.vl.cluster.domain.exception.WrongCredentialsException

sealed interface NetworkAuth {

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
}