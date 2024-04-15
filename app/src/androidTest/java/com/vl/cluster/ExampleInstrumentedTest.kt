package com.vl.cluster

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vl.cluster.api.network.tg.TelegramNetwork

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.File
import java.io.PrintWriter
import kotlin.math.log

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val session = TelegramNetwork(InstrumentationRegistry.getInstrumentation().context).signIn()
    }

    @Test
    fun saveFileTest() {
        val datadir = InstrumentationRegistry.getInstrumentation().context.dataDir
        PrintWriter(File(datadir, "sucksomedick").outputStream()).use {it.println("XD?")}
    }
}