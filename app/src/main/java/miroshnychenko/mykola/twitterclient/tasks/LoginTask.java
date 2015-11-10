package miroshnychenko.mykola.twitterclient.tasks;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.events.BaseEvent;
import miroshnychenko.mykola.twitterclient.http.LoginApi;
import miroshnychenko.mykola.twitterclient.models.Login;
import miroshnychenko.mykola.twitterclient.tasks.base.BaseResultAsyncTask;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by nsmirosh on 11/10/2015.
 */
public class LoginTask  extends BaseResultAsyncTask<Login, Response> {

    public static final String TAG = LoginTask.class.getSimpleName();

    @Inject
    LoginApi mLoginApi;

    @Override
    protected Response getResult(Login... params) throws RetrofitError {
        return mLoginApi.login(params[0].getCallback(),
                params[0].getConsumerKey(),
                params[0].getNonce(),
                params[0].getSignature(),
                params[0].getTimestamp());
    }

    @Override
    protected BaseEvent<Response> createBaseEvent(Response result, RetrofitError exception) {
        return new LoginEvent(result, exception);
    }

    public static class LoginEvent extends BaseEvent<Response> {
        public LoginEvent(Response result, RetrofitError exception) {
            super(result, exception);
        }
    }


}
