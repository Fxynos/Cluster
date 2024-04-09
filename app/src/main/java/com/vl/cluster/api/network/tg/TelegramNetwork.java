package com.vl.cluster.api.network.tg;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vl.cluster.api.ApiCredentialsKt;
import com.vl.cluster.api.definition.Network;
import com.vl.cluster.api.definition.Session;
import com.vl.cluster.api.definition.features.NetworkAuth;
import com.vl.cluster.api.definition.pojo.Post;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class TelegramNetwork implements Network<TelegramNetwork.TelegramSession>, NetworkAuth.Sms<TelegramNetwork.TelegramSession>, OnAuthListener{
    private static Client client = null;

    private static final String NAME = "Telegram";

    private static TdApi.AuthorizationState authorizationState = null;
    private static final Lock authorizationLock = new ReentrantLock();
    private static final Condition gotAuthorization = authorizationLock.newCondition();

    private static volatile boolean haveAuthorization = false;
    private static volatile boolean needQuit = false;
    private static volatile boolean canQuit = false;

    private static final String newLine = System.getProperty("line.separator");

    private final Context context;

    private final OnAuthNetworkListener listener;

    public TelegramNetwork(Context context, OnAuthNetworkListener listener) {
        this.context = context;
        context.getFilesDir().mkdirs();
        this.listener = listener;
    }

    @NonNull
    @Override
    public NetworkAuth<TelegramSession> getAuthentication() {
        return this;
    }

    @NonNull
    @Override
    public String getNetworkName() {
        return NAME;
    }

    @NonNull
    @Override
    public Set<LoginType> getLoginVariants() {
        return Set.of(LoginType.PHONE);
    }

    @Override
    public int getNetworkId() {
        return Network.DefaultImpls.getNetworkId(this);
    }

    @Override
    public Long getNextRequestAvailableAt() {
        return null;
    }

    @Override
    public Integer requestCode() {
        return null;
    }

    public TelegramSession signIn() throws InterruptedException {
        client = Client.create(new UpdateHandler(), null, null);
        Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));
        // Handle if not authenticated
        phoneNumber = listener.requestedPhoneNumber();
        return new TelegramSession(TelegramNetwork.client);
    }


    private void onAuthorizationStateUpdated(TdApi.AuthorizationState authorizationState) {
        if (authorizationState != null) {
            TelegramNetwork.authorizationState = authorizationState;
        }
        switch (TelegramNetwork.authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();
                parameters.databaseDirectory = context.getApplicationInfo().dataDir.concat("/");
                parameters.useMessageDatabase = false; //  If set to true, the library will maintain a local cache of chats and messages.
                parameters.deviceModel = "Android";
                parameters.apiHash = ApiCredentialsKt.API_HASH_TG;
                parameters.apiId = ApiCredentialsKt.API_ID_TG;
                parameters.systemLanguageCode = "en";
                parameters.useSecretChats = false;
                parameters.applicationVersion = "1.0";
                parameters.filesDirectory = "";
                client.send(new TdApi.SetTdlibParameters(parameters), new AuthorizationRequestHandler());
                break;
            case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                client.send(new TdApi.CheckDatabaseEncryptionKey(), new AuthorizationRequestHandler());
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR: {
                // TODO Need a phone number
                client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR: {
                String link = ((TdApi.AuthorizationStateWaitOtherDeviceConfirmation) TelegramNetwork.authorizationState).link;
                // TODO Waiting for confirm here from other device
                break;
            }
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR: {
                // TODO Receive AuthCode
                Log.i("TelegramNetwork", "Requires code");
                client.send(new TdApi.CheckAuthenticationCode(listener.requestedCode()), new AuthorizationRequestHandler());
               // client.send(new TdApi.CheckAuthenticationCode("code"), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR: {
                // TODO Waiting for First, Last name
                client.send(new TdApi.RegisterUser("firstName", "lastName"), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR: {
                // TODO Waiting for password
                client.send(new TdApi.CheckAuthenticationPassword("password"), new AuthorizationRequestHandler());
                break;
            }
            case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                haveAuthorization = true;
                authorizationLock.lock();
                try {
                    gotAuthorization.signal();
                } finally {
                    authorizationLock.unlock();
                }
                break;
            case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR:
                haveAuthorization = false;
                break;
            case TdApi.AuthorizationStateClosing.CONSTRUCTOR:
                haveAuthorization = false;
                break;
            case TdApi.AuthorizationStateClosed.CONSTRUCTOR:
                if (!needQuit) {
                    client = Client.create(new UpdateHandler(), null, null); // recreate client after previous has closed
                } else {
                    canQuit = true;
                }
                break;
            default:
                System.err.println("Unsupported authorization state:" + newLine + TelegramNetwork.authorizationState);
        }
    }

    private static class AuthorizationRequestHandler implements Client.ResultHandler {

        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.Error.CONSTRUCTOR:
                    Log.e("TelegramNetwork", "handling error");
                    break;
                case TdApi.Ok.CONSTRUCTOR:
                    // nothing to do.
                    break;
                default:
                    // wrong response from TDLib error
            }
        }
    }

    public class TelegramSession extends Session {


        private Client sessionClient;

        private TelegramSession(Client sessionClient) {
            super(TelegramNetwork.this);
            this.sessionClient = sessionClient;
        }


        @Override
        public int getSessionId() {
            return 0;
        }

        @NonNull
        @Override
        public String getSessionName() {
            return null;
        }

        @NonNull
        @Override
        public List<Post> nextPage(int count, @Nullable String key) {
            return null;
        }
    }

    private class UpdateHandler implements Client.ResultHandler {

        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                    onAuthorizationStateUpdated(((TdApi.UpdateAuthorizationState) object).authorizationState);
                    break;
                // TODO [fae] Handle updates
            }
        }
    }

}

