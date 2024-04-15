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
            Log.d(TAG, "Requesting code...");
            var codeInfo = network.requestCode("9996612222");
            Log.d(TAG, "Code: ".concat(codeInfo.toString()));
            try {
                network.signIn("1111");
                Log.d(TAG, "Logged");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}