package miroshnychenko.mykola.twitterclient.http;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;


import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.SSLException;

import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;



/**
 * Created by Thomas on 8/16/14.
 */
@Singleton
public class TwitterOkClient extends OkClient {

    public static final String HTTP_HEADER_AUTH = "Authorization";
    public static final String LOGOUT_EVENT = "LogoutEvent";

    @Inject
    PreferenceUtils mPreferenceUtils;
//    @Inject
//    javax.inject.Provider<LoginUtils> mLoginUtilsProvider;
    @Inject
    Context mContext;

    @Inject
    public TwitterOkClient(OkHttpClient client) {
        super(client);
    }


    @Override
    public Response execute(Request request) throws IOException {

        request = buildRequest(request);

        Response response;
        try {
            response = super.execute(request);
        } catch (UnknownHostException | SSLException | java.net.SocketException e) {
            throw RetrofitError.networkError(request.getUrl(), e);
        }
//
//        Login login = mPreferenceUtils.getLogin();
//        if(response.getStatus() == 401 && login != null && !isRegisterUrl(request)) {
//
//            // trying to log in again
//            if(login.facebookToken != null) {
//                mLoginUtilsProvider.get().facebookLogin(login.facebookToken);
//            }
//            else {
//                mLoginUtilsProvider.get().login(login);
//            }
//
//            return super.execute(
//                    buildRequest(request)
//            );
//        }

        if(response.getStatus() == 400 && isLoginUrl(request)) {
            sendLogoutBroadcast();
        }

        return response;
    }

    private Request buildRequest(Request request) {
        if(!mPreferenceUtils.hasToken()) {
            return request;
        }
        List<Header> headers1 = new ArrayList<>();
        String tokenStr = mPreferenceUtils.getToken().getToken();
        Header header = new Header(HTTP_HEADER_AUTH, "Bearer " + tokenStr);
        headers1.add(header);

        for (Header header1 : request.getHeaders()) {
            if(!header1.getName().equals(HTTP_HEADER_AUTH)) {
                headers1.add(header1);
            }
        }

        return new Request(request.getMethod(), request.getUrl(), headers1, request.getBody());
    }

    private void sendLogoutBroadcast() {
        Intent intent = new Intent(LOGOUT_EVENT);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private boolean isRegisterUrl(Request request) {
        return request.getUrl().contains("/Account/Register") ||
                request.getUrl().contains("/Account/token") ||
                request.getUrl().contains("/Account/RegisterExternal") ||
                request.getUrl().contains("/Account/ObtainLocalAccessToken");
    }

    private boolean isLoginUrl(Request request) {
        return request.getUrl().contains("/Account/token") ||
            request.getUrl().contains("/Account/ObtainLocalAccessToken");
    }

}
