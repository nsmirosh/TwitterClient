package miroshnychenko.mykola.twitterclient.http;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by nsmirosh on 11/10/2015.
 */
public interface LoginApi {

    @POST("/oauth/request_token")
    @Headers({
            "oauth_signature_method: HMAC-SHA1",
            "oauth_version: 1.0"
    })

    Response login(@Header("oauth_callback") String callback,
                   @Header("oauth_consumer_key") String consumerKey,
                   @Header("oauth_nonce") String nonce,
                   @Header("oauth_signature") String signature,
                   @Header("oauth_timestamp") Long timeStamp);



}
