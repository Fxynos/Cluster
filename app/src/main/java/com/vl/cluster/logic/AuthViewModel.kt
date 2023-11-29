package com.vl.cluster.logic

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vl.cluster.GlobalState
import com.vl.cluster.api.definition.exception.CaptchaException
import com.vl.cluster.api.definition.exception.ConnectionException
import com.vl.cluster.api.definition.exception.TwoFaException
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException
import com.vl.cluster.api.definition.features.NetworkAuth
import com.vl.cluster.screen.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

class AuthViewModel(app: Application): AndroidViewModel(app) {
    /* State */
    lateinit var network: Network
    val login = mutableStateOf("")
    val password = mutableStateOf("")

    @Throws(MalformedInputException::class)
    fun attemptLogin() {
        val isValid = GlobalState.reducer.findNetById(network.id).authentication.loginVariants.any {
            when (it) {
                NetworkAuth.LoginType.ID -> login.value.isNotBlank()
                NetworkAuth.LoginType.EMAIL -> login.value
                    .matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))

                NetworkAuth.LoginType.PHONE -> login.value
                    .matches(Regex("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}\$"))
            }
        }
        if (!isValid)
            throw MalformedInputException()
    }

    @Throws(
        MalformedInputException::class,
        NetworkAuth.Password.WrongCredentialsException::class,
        ConnectionException::class,
        TwoFaException::class,
        CaptchaException::class,
        UnsupportedLoginMethodException::class
    )
    fun attemptPassword() {
        if (password.value.isBlank())
            throw MalformedInputException()
        runBlocking(Dispatchers.IO) { // TODO async
            GlobalState.reducer.signInWithPassword(network.id, login.value, password.value)
        }
    }
    class MalformedInputException: Exception()
}