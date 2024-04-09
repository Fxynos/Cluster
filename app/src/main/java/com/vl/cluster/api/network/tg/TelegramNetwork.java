package com.vl.cluster.api.network.tg;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vl.cluster.api.ApiCredentialsKt;
import com.vl.cluster.api.definition.Network;
import com.vl.cluster.api.definition.Session;
import com.vl.cluster.api.definition.SessionStore;
import com.vl.cluster.api.definition.entity.Comment;
import com.vl.cluster.api.definition.entity.Page;
import com.vl.cluster.api.definition.entity.Post;
import com.vl.cluster.api.definition.entity.Profile;
import com.vl.cluster.api.definition.exception.CaptchaException;
import com.vl.cluster.api.definition.exception.ConnectionException;
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException;
import com.vl.cluster.api.definition.exception.WrongCredentialsException;
import com.vl.cluster.api.definition.feature.Messenger;
import com.vl.cluster.api.definition.feature.NetworkAuth;
import com.vl.cluster.api.definition.feature.Newsfeed;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class TelegramNetwork implements Network, NetworkAuth.CodeAuth {
    private static Client client = null;

    private static final String NAME = "Telegram";

    private static TdApi.AuthorizationState authorizationState = null;

    private static volatile boolean haveAuthorization = false;
    private static volatile boolean needQuit = false;
    private static volatile boolean canQuit = false;

    private static final String newLine = System.getProperty("line.separator");

    private final Context context;

    /* Locks */
    private final Lock authorizationLock = new ReentrantLock();
    private final Condition gotAuthorization = authorizationLock.newCondition();
    private final BlockingQueue<CodeInfo> codeQueue = new LinkedBlockingQueue<>();
    private final Object authLock = new Object();

    public TelegramNetwork(Context context) {
        this.context = context;
        client = Client.create(new UpdateHandler(), null, null);
        Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false)));
    }

    @NonNull
    @Override
    public NetworkAuth getAuthentication() {
        return this;
    }

    @NonNull
    @Override
    public SessionStore getSessionStore() {
        return null; // TODO
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

    /**
     * @param login phone number
     */
    @NonNull
    @Override
    public CodeInfo requestCode(@NonNull String login) {
        requireState(TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR);

        client.send(
                new TdApi.SetAuthenticationPhoneNumber(login, null),
                new AuthorizationRequestHandler()
        );

        try {
            return codeQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    @Override
    public Session signIn(@NonNull String code) throws WrongCredentialsException, ConnectionException, CaptchaException, UnsupportedLoginMethodException {
        requireState(TdApi.AuthorizationStateWaitCode.CONSTRUCTOR);

        client.send(
                new TdApi.CheckAuthenticationCode(code),
                new AuthorizationRequestHandler()
        );

        try {
            authLock.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        requireState(TdApi.AuthorizationStateReady.CONSTRUCTOR);

        return new TelegramSession(client);
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
                var codeInfo = ((TdApi.AuthorizationStateWaitCode) Objects.requireNonNull(
                        authorizationState
                )).codeInfo;
                try {
                    codeQueue.put(createCodeInfo(codeInfo));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
                System.err.println("Unsupported authorization state:\n".concat(
                    TelegramNetwork.authorizationState.toString()
                ));
        }
    }

    /* Utils */

    private static void requireState(int constructor) {
        if (authorizationState.getConstructor() != constructor)
            throw new IllegalStateException("State: ".concat(authorizationState.toString()));
    }

    private static CodeInfo createCodeInfo(TdApi.AuthenticationCodeInfo codeType) {
        return switch (codeType.type.getConstructor()) {
            case TdApi.AuthenticationCodeTypeTelegramMessage.CONSTRUCTOR -> {
                var concreteType = (TdApi.AuthenticationCodeTypeTelegramMessage) codeType.type;
                yield new CodeInfo(concreteType.length, CodeLocation.APP, codeType.timeout);
            }

            case TdApi.AuthenticationCodeTypeSms.CONSTRUCTOR -> {
                var concreteType = (TdApi.AuthenticationCodeTypeSms) codeType.type;
                yield new CodeInfo(concreteType.length, CodeLocation.SMS, codeType.timeout);
            }

            case TdApi.AuthenticationCodeTypeCall.CONSTRUCTOR -> {
                var concreteType = (TdApi.AuthenticationCodeTypeCall) codeType.type;
                yield new CodeInfo(concreteType.length, CodeLocation.CALL, codeType.timeout);
            }

            // TODO [tva] enter automatically
            case TdApi.AuthenticationCodeTypeFlashCall.CONSTRUCTOR -> {
                var concreteType = (TdApi.AuthenticationCodeTypeFlashCall) codeType.type;
                yield new CodeInfo(concreteType.pattern.length(), CodeLocation.CALL, codeType.timeout);
            }

            // TODO [tva] use phone number prefix
            case TdApi.AuthenticationCodeTypeMissedCall.CONSTRUCTOR -> {
                var concreteType = (TdApi.AuthenticationCodeTypeMissedCall) codeType.type;
                yield new CodeInfo(concreteType.length, CodeLocation.APP, codeType.timeout);
            }

            default -> throw new RuntimeException(); // unreachable
        };
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

    public class TelegramSession implements Session {

        private final Client client;

        private TelegramSession(Client client) {
            this.client = client;
        }

        @NonNull
        @Override
        public Network getNetwork() {
            return TelegramNetwork.this;
        }

        @NonNull
        @Override
        public Newsfeed getNewsfeed() {
            return this;
        }

        @NonNull
        @Override
        public Messenger getMessenger() {
            return this;
        }

        @Override
        public int getSessionId() {
            return 0; // TODO
        }

        @NonNull
        @Override
        public String getSessionName() {
            return null; // TODO
        }

        @NonNull
        @Override
        public Page<String, Post> fetchNews(@Nullable Profile source, int count, @Nullable String key) {
            return null; // TODO
        }

        @NonNull
        @Override
        public Page<String, Comment> fetchComments(@NonNull Post post, int count, @Nullable String key) {
            return null; // TODO
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

