package iweinzierl.github.com.moviedatabase.rest.security;

import android.content.Context;
import android.util.Base64;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class BasicAuthInterceptor implements Interceptor {

    private final Context context;
    private final CredentialsStore credentialsStore;

    public BasicAuthInterceptor(Context context) {
        this.context = context;
        this.credentialsStore = new CredentialsStore(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .addHeader(
                        "Authorization",
                        createAuthorizationHeaderValue())
                .method(request.method(), request.body())
                .build();

        return chain.proceed(authenticatedRequest);
    }

    private String createAuthorizationHeaderValue() {
        Credentials credentials = credentialsStore.getCredentials();

        String username = credentials.getUsername();
        String password = credentials.getPassword();
        String combined = String.format("%s:%s", username, password);

        return String.format("Basic %s", Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP));
    }
}
