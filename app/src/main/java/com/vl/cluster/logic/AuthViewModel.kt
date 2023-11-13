package com.vl.cluster.logic

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vl.cluster.api.definition.exception.CaptchaException
import com.vl.cluster.api.definition.exception.ConnectionException
import com.vl.cluster.api.definition.exception.TwoFaException
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException
import com.vl.cluster.api.definition.features.NetworkAuth
import com.vl.cluster.screen.Network
import kotlin.jvm.Throws

class AuthViewModel(app: Application): AndroidViewModel(app) {
    enum class Destinations {
        LOGIN,
        PASSWORD,
        TWO_FA,
        APPLY
    }

    sealed interface Popup {
        class ConnectionError(val description: String): Popup
        class Captcha(val url: String): Popup
        class UnsupportedLoginMethodError(val description: String): Popup
        object WrongCredentialsError: Popup
    }

    /* Navigation */
    val destination = MutableLiveData(Destinations.LOGIN)
    val popup = MutableLiveData<Popup>(null)

    /* State */
    lateinit var network: Network
    val login = mutableStateOf("")
    val password = mutableStateOf("")

    @Throws(
        NetworkAuth.Password.WrongCredentialsException::class,
        ConnectionException::class,
        TwoFaException::class,
        CaptchaException::class,
        UnsupportedLoginMethodException::class
    )
    fun signIn() {
        println("signIn: ${network.name}, ${login.value}, ${password.value}")
    }
}