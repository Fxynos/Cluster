package com.vl.cluster;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.vl.cluster.api.definition.exception.CaptchaException;
import com.vl.cluster.api.definition.exception.ConnectionException;
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException;
import com.vl.cluster.api.definition.exception.WrongCredentialsException;
import com.vl.cluster.api.network.tg.TelegramNetwork;

import java.util.concurrent.CompletableFuture;


public class TestActivity extends ComponentActivity {

    private final static String TAG = "Test Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelegramNetwork network = new TelegramNetwork(this);
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Log.d(TAG, "Requesting code...");
            var codeInfo = network.requestCode("9996621262");
            Log.d(TAG, "Code: ".concat(codeInfo.toString()));
            try {
                Thread.sleep(1000);
                Log.d(TAG, "Passing the code...");
                network.signIn("22222");
                Log.d(TAG, "Signed in");
            } catch (Exception e) {
                Log.e(TAG, "Interrupted: ".concat(e.getMessage()));
            }
        });
    }

}