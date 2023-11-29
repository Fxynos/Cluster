package com.vl.cluster.api.definition.exception

import com.vl.cluster.api.definition.Session
import kotlin.jvm.Throws

class CaptchaException(
    val id: String,
    val url: String,
    private val onConfirmed: Confirmation
): Exception() {
    @Throws(Exception::class)
    fun confirm(captcha: String) = onConfirmed.confirm(captcha)

    fun interface Confirmation {
        /**
         * @throws CaptchaException if captcha is wrong
         * @throws ConnectionException on transport client error
         * @throws Exception if any other exception occurs. For example, if user signed in with wrong credentials
         */
        @Throws(Exception::class)
        fun confirm(captcha: String): Session
    }
}