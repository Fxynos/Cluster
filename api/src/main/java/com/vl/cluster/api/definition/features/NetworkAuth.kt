package com.vl.cluster.api.definition.features

import com.vl.cluster.api.definition.ConnectionException
import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.network.vk.VkNetwork.Fa2Exception
import com.vl.cluster.api.network.vk.VkNetwork.NeedCaptchaException
import com.vl.cluster.api.network.vk.VkNetwork.NeedFa2CallReset

sealed interface NetworkAuth<S: Session> {
    enum class LoginType {
        PHONE,
        EMAIL,
        ID
    }

    val loginVariants: Set<LoginType>

    interface Password<S: Session>: NetworkAuth<S> {
        @Throws(WrongCredentialsException::class, ConnectionException::class, Fa2Exception::class, NeedCaptchaException::class, NeedFa2CallReset::class)
        fun signIn(login: String, password: String): S

        class WrongCredentialsException: Exception()
    }

    interface Sms<S: Session>: NetworkAuth<S> // TODO

    interface Call<S: Session>: NetworkAuth<S> // TODO
}