package com.vl.cluster.logic

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vl.cluster.screen.Network

class AuthViewModel: ViewModel() {
    lateinit var network: Network
    val login = mutableStateOf("")
    val password = mutableStateOf("")

    fun signIn() {
        println("signIn: ${network.name}, ${login.value}, ${password.value}")
    }
}