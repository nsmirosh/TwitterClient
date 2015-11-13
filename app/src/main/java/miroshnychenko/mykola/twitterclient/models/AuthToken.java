package miroshnychenko.mykola.twitterclient.models;

/**
 * Created by nsmirosh on 11/10/2015.
 */
public class AuthToken {

    private String token;
    private String secret;

    public AuthToken(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
