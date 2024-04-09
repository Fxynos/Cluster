package com.vl.cluster

import android.app.Application
import com.vl.cluster.api.NetworkReducer
import com.vl.cluster.api.network.vk.VkNetwork

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalState.reducer = NetworkReducer(VkNetwork(applicationContext))
    }
}