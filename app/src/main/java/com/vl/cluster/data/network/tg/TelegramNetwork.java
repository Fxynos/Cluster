package com.vl.cluster.data.network.tg;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vl.cluster.ApiCredentialsKt;
import com.vl.cluster.R;
import com.vl.cluster.domain.boundary.Network;
import com.vl.cluster.domain.boundary.Session;
import com.vl.cluster.domain.boundary.SessionStore;
import com.vl.cluster.domain.entity.ChatDialog;
import com.vl.cluster.domain.entity.ChatMessage;
import com.vl.cluster.domain.entity.CodeLocation;
import com.vl.cluster.domain.entity.Comment;
import com.vl.cluster.domain.entity.Dialog;
import com.vl.cluster.domain.entity.LoginType;
import com.vl.cluster.domain.entity.Page;
import com.vl.cluster.domain.entity.Post;
import com.vl.cluster.domain.entity.PrivateDialog;
import com.vl.cluster.domain.entity.PrivateMessage;
import com.vl.cluster.domain.entity.Profile;
import com.vl.cluster.domain.exception.CaptchaException;
import com.vl.cluster.domain.exception.ConnectionException;
import com.vl.cluster.domain.exception.UnsupportedLoginMethodException;
import com.vl.cluster.domain.exception.WrongCredentialsException;
import com.vl.cluster.domain.boundary.Messenger;
import com.vl.cluster.domain.boundary.NetworkAuth;
import com.vl.cluster.domain.boundary.Newsfeed;

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

    private final static String TAG = "Telegram Network";
    private static final String NAME = "Telegram";

    private static Client client = null;
    private static TdApi.AuthorizationState authorizationState = null;

    private static volatile boolean haveAuthorization = false;
    private static volatile boolean needQuit = false; // TODO [tva] remove if redundant
    private static volatile boolean canQuit = false;


    private final Context context;

    /* Locks */
    private final BlockingQueue<CodeInfo> codeQueue = new LinkedBlockingQueue<>();
    private final Lock authLock = new ReentrantLock();
    private final Condition authCondition = authLock.newCondition();

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
        return () -> Set.of(); // TODO replace stub with implementation
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
        return getNetworkName().hashCode();
    }

    @Override
    public int getIcon() {
        return R.drawable.telegram;
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
                new AuthorizationRequestHandler("Phone")
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
                new AuthorizationRequestHandler("Auth Code")
        );

        authLock.lock();
        try {
            authCondition.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            authLock.unlock();
        }

        try {
            requireState(TdApi.AuthorizationStateReady.CONSTRUCTOR);
        } catch (IllegalStateException exception) {
            throw new WrongCredentialsException();
        }

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
                parameters.useTestDc = true;
                parameters.apiId = ApiCredentialsKt.API_ID_TG;
                parameters.systemLanguageCode = "en";
                parameters.useSecretChats = false;
                parameters.applicationVersion = "1.0";
                parameters.filesDirectory = "";
                client.send(
                        new TdApi.SetTdlibParameters(parameters),
                        new AuthorizationRequestHandler("TDLib Params")
                );
                break;
            case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                client.send(
                        new TdApi.CheckDatabaseEncryptionKey(),
                        new AuthorizationRequestHandler("Encryption Key")
                );
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
                client.send(
                        new TdApi.RegisterUser("firstName", "lastName"),
                        new AuthorizationRequestHandler("Name")
                );
                break;
            }
            case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR: {
                // TODO Waiting for password
                client.send(
                        new TdApi.CheckAuthenticationPassword("password"),
                        new AuthorizationRequestHandler("Password")
                );
                break;
            }
            case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                haveAuthorization = true;
                authLock.lock();
                authCondition.signal();
                authLock.unlock();
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

    private class AuthorizationRequestHandler implements Client.ResultHandler {

        private final String tag;

        public AuthorizationRequestHandler(String tag) {
            this.tag = tag;
        }

        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.Error.CONSTRUCTOR:
                    switch (authorizationState.getConstructor()) {
                        case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                            authLock.lock();
                            authCondition.signal();
                            authLock.unlock();
                        }
                        default -> Log.e(TAG, tag.concat(": Failed"));
                    }
                    break;
                case TdApi.Ok.CONSTRUCTOR:
                    Log.d(TAG, tag.concat(": Success"));
                    break;
                default:
                    // wrong response from TDLib error
            }
        }
    }

    public class TelegramSession extends Session {

        private final Client client;

        private TelegramSession(Client client) {
            this.client = client;
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

        @NonNull
        @Override
        public Dialog getDialog(long dialogId) {
            return null;
        }

        @NonNull
        @Override
        public Page<String, Dialog> fetchDialogs(int count, @Nullable String key) {
            return null;
        }

        @NonNull
        @Override
        public Page<String, PrivateMessage> fetchMessages(@NonNull PrivateDialog dialog, int count, @Nullable String key) {
            return null;
        }

        @NonNull
        @Override
        public Page<String, ChatMessage> fetchMessages(@NonNull ChatDialog dialog, int count, @Nullable String key) {
            return null;
        }

        @NonNull
        @Override
        public PrivateMessage sendMessage(@NonNull PrivateMessage message) {
            return null;
        }

        @NonNull
        @Override
        public ChatMessage sendMessage(@NonNull ChatMessage message) {
            return null;
        }

        @NonNull
        @Override
        public Post getPost(long postId) {
            return null;
        }

        @NonNull
        @Override
        public Comment getComment(long commentId) {
            return null;
        }

        @NonNull
        @Override
        public Profile getProfile(long profileId) {
            return null;
        }

        @NonNull
        @Override
        public Page<String, Post> fetchNews(@Nullable Profile source, int count, @Nullable String key) {
            return null;
        }

        @NonNull
        @Override
        public Page<String, Comment> fetchComments(@NonNull Post post, int count, @Nullable String key) {
            return null;
        }

        @NonNull
        @Override
        public Post setLike(@NonNull Post post) {
            return null;
        }

        @NonNull
        @Override
        public Comment leaveComment(@NonNull Comment comment) {
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

