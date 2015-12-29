package iweinzierl.github.com.moviedatabase.rest.security;

public class Credentials {

    private final String username;
    private final String password;
    private final boolean rememberMe;

    public Credentials(String username, String password, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }
}
