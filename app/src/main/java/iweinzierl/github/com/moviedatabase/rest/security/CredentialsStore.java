package iweinzierl.github.com.moviedatabase.rest.security;

import android.content.Context;
import android.content.SharedPreferences;

public class CredentialsStore {

    private static final String SHARED_PREFS_CREDENTIALS = "com.github.iweinzierl.moviedatabase.credentialsstore";
    private static final String PREF_USERNAME = "pref.username";
    private static final String PREF_PASSWORD = "pref.password";
    private static final String PREF_REMEMBERME = "pref.rememberme";

    private final Context context;

    public CredentialsStore(Context context) {
        this.context = context;
    }

    public void setCredentials(Credentials credentials) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_CREDENTIALS, Context.MODE_PRIVATE);
        prefs.edit().putString(PREF_USERNAME, credentials.getUsername()).apply();
        prefs.edit().putString(PREF_PASSWORD, credentials.getPassword()).apply();
        prefs.edit().putBoolean(PREF_REMEMBERME, credentials.isRememberMe()).apply();
    }

    public Credentials getCredentials() {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_CREDENTIALS, Context.MODE_PRIVATE);
        return new Credentials(
                prefs.getString(PREF_USERNAME, null),
                prefs.getString(PREF_PASSWORD, null),
                prefs.getBoolean(PREF_REMEMBERME, false)
        );
    }
}
