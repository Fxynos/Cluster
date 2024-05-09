package com.vl.cluster.presentation

import android.app.Application
import com.vl.cluster.domain.manager.NetworkReducer
import com.vl.cluster.data.network.vk.VkNetwork

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalState.reducer = NetworkReducer(VkNetwork(applicationContext))
    }
}