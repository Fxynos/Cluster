package com.vl.cluster.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vl.cluster.R
import com.vl.cluster.domain.entity.LoginType
import com.vl.cluster.domain.exception.ApiCustomException
import com.vl.cluster.domain.exception.CaptchaException
import com.vl.cluster.domain.exception.ConnectionException
import com.vl.cluster.domain.exception.WrongCredentialsException
import com.vl.cluster.domain.manager.AuthManager
import com.vl.cluster.presentation.entity.NetworkData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AuthViewModel.Factory::class)
class AuthViewModel @AssistedInject constructor(
    app: Application,
    @Assisted networkId: Int,
    private val authManager: AuthManager
): AndroidViewModel(app) {
    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val context: Context
        get() = getApplication()

    private val network = authManager.networks.first { it.networkId == networkId }
    private lateinit var login: String
    private lateinit var captcha: CaptchaException

    val networkData = NetworkData(network.networkName, network.networkId, network.icon)
    val loginVariant: LoginType? // null if there are several login types
        get() = network.authentication.loginVariants.let {
            if (it.size > 1)
                null
            else
                it.first()
        }

    private val _uiState = MutableStateFlow<UiState>(UiState.Authentication)
    val uiState: StateFlow<UiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            uiState.collect { Log.d(TAG, "UI State: $it") }
        }
    }

    fun attemptLogin(login: String) {
        val state = uiState.value
        if (state !is UiState.Authentication)
            throw IllegalStateException(state.toString())

        val isValid = network.authentication.loginVariants.any {
            when (it) {
                LoginType.ID -> login.isNotBlank()
                LoginType.EMAIL -> login.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
                LoginType.PHONE -> login.matches(Regex("^(\\+7|8)[0-9]{10}\$"))
            }
        }

        if (isValid)
            this.login = login
        else
            _uiState.update { UiState.Error(context.getString(R.string.error_malformed_login)) }
    }

    fun attemptPassword(password: String) {
        val state = uiState.value
        if (state !is UiState.Authentication)
            throw IllegalStateException(state.toString())

        if (password.isBlank()) {
            _uiState.update { UiState.Error(context.getString(R.string.error_wrong_credentials)) }
            return
        }
        _uiState.update { UiState.Processing }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authManager.signInWithPassword(network.networkId, login, password)
                _uiState.update { UiState.Authenticated }
            } catch (e: Exception) {
                e.printStackTrace()
                handle(e)
            }
        }
    }

    fun attemptCaptcha(captcha: String) {
        val state = uiState.value
        if (state !is UiState.Captcha)
            throw IllegalStateException(state.toString())

        viewModelScope.launch(Dispatchers.IO) {
            try {
                this@AuthViewModel.captcha.confirm(captcha)
                _uiState.update { UiState.Authenticated }
            } catch (e: Exception) {
                e.printStackTrace()
                handle(e)
            }
        }
    }

    /**
     * Reset error state or cancel captcha
     */
    fun retry() {
        val state = uiState.value
        if (state !is UiState.Popup && state !is UiState.Captcha && state !is UiState.Error)
            throw IllegalStateException(state.toString())
        _uiState.update { UiState.Authentication }
    }

    private fun handle(e: Exception) {
        when (e) {
            is CaptchaException -> {
                captcha = e
                _uiState.update { UiState.Captcha(e.url) }
            }
            is WrongCredentialsException ->
                _uiState.update { UiState.Error(context.getString(R.string.error_wrong_credentials)) }
            is ConnectionException ->
                _uiState.update {
                    UiState.Popup(
                        context.getString(R.string.error_title),
                        context.getString(R.string.error_connection)
                    )
                }
            is ApiCustomException ->
                _uiState.update { UiState.Popup(e.title, e.description) }
            else ->
                _uiState.update {
                    UiState.Popup(
                        context.getString(R.string.error_title),
                        e.localizedMessage ?: e.stackTraceToString()
                    )
                }
        }
    }

    sealed interface UiState {
        data object Authentication: UiState
        data class Error(val hint: String): UiState
        data object Processing: UiState
        data class Captcha(val imageUrl: String): UiState
        data class Popup(val title: String, val description: String): UiState
        data object Authenticated: UiState
    }

    @AssistedFactory
    interface Factory {
        fun create(networkId: Int): AuthViewModel
    }
}