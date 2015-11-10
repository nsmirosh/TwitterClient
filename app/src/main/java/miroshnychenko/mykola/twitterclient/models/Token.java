package miroshnychenko.mykola.twitterclient.models;

/**
 * Created by nsmirosh on 11/10/2015.
 */
public class Token {

    private String token;
    private String tokenSecret;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }
}
