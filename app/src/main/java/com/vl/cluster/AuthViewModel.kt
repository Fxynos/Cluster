package com.vl.cluster

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.vl.cluster.api.definition.exception.CaptchaException
import com.vl.cluster.api.definition.exception.ConnectionException
import com.vl.cluster.api.definition.exception.TwoFaException
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException
import com.vl.cluster.api.definition.exception.WrongCredentialsException
import com.vl.cluster.api.definition.feature.NetworkAuth
import com.vl.cluster.screen.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.jvm.Throws

class AuthViewModel(app: Application): AndroidViewModel(app) {
    /* State */
    lateinit var network: Network
    val login = mutableStateOf("")
    val password = mutableStateOf("")
    val loginVariant: NetworkAuth.LoginType? // null if there are several login types
        get() = GlobalState.reducer.findNetById(network.id).authentication.loginVariants.let {
            if (it.size > 1)
                null
            else
                it.first()
        }

    @Throws(MalformedInputException::class)
    fun attemptLogin() {
        val isValid = GlobalState.reducer.findNetById(network.id).authentication.loginVariants.any {
            when (it) {
                NetworkAuth.LoginType.ID -> login.value.isNotBlank()
                NetworkAuth.LoginType.EMAIL -> login.value
                    .matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))

                NetworkAuth.LoginType.PHONE -> login.value
                    .matches(Regex("^(\\+7|8)[0-9]{10}\$"))
            }
        }
        if (!isValid)
            throw MalformedInputException()
    }

    @Throws(
        MalformedInputException::class,
        WrongCredentialsException::class,
        ConnectionException::class,
        TwoFaException::class,
        CaptchaException::class,
        UnsupportedLoginMethodException::class
    )
    fun attemptPassword() {
        if (password.value.isBlank())
            throw MalformedInputException()
        runBlocking(Dispatchers.IO) { // TODO [tva] async
            GlobalState.reducer.signInWithPassword(network.id, login.value, password.value)
        }
    }

    class MalformedInputException: Exception()
}