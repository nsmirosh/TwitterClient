package miroshnychenko.mykola.twitterclient.modules;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import javax.inject.Qualifier;
import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import dagger.Module;
import dagger.Provides;
import miroshnychenko.mykola.twitterclient.BuildConfig;
import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.activities.MainActivity;
import miroshnychenko.mykola.twitterclient.activities.StartUpActivity;
import miroshnychenko.mykola.twitterclient.http.LoginApi;
import miroshnychenko.mykola.twitterclient.http.RestAPI;
import miroshnychenko.mykola.twitterclient.misc.AndroidBus;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;
import retrofit.Endpoint;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by nsmirosh on 7/8/2015.
 */

@Module(
        complete = false,
        library = true,
        injects = {
                TwitterApplication.class, StartUpActivity.class, PreferenceUtils.class, MainActivity.class
        }
)
public class TwitterContextModule {

    public static final String RETROFIT_TAG = "retrofit";

    Context mContext;

    public TwitterContextModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    Resources provideResources() {
        return mContext.getResources();
    }



    @Provides
    AssetManager provideAssetManager() {
        return mContext.getAssets();
    }

    @Provides
    LayoutInflater provideLayoutInflater(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Provides @Singleton
    Gson provideGson() {
        return GsonFactory.getGson();
    }

    @Provides
    @Singleton
    GsonConverter provideGsonConverter(Gson gson) {
        return new GsonConverter(gson);
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new AndroidBus(ThreadEnforcer.ANY);
    }

    @Provides
    LoginApi provideLoginApi(RestAdapter restAdapter) {
        return restAdapter
                .create(LoginApi.class);
    }

    @Provides
    Endpoint provideEndpoint() {
        return new Endpoint() {
            @Override
            public String getUrl() {
                return RestAPI.URLBASE;
            }

            @Override
            public String getName() {
                return "default";
            }
        };
    }



    @Provides
    RestAdapter provideRestAdapter(GsonConverter gsonConverter, /*DitioOkClient ditioOkClient,*/  Endpoint endpoint) {

        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        Log.d(TwitterContextModule.class.getSimpleName(), "message: " + message);
                    }
                })
                .setLogLevel(BuildConfig.LOG ? RestAdapter.LogLevel.valueOf(BuildConfig.LOG_LEVEL) : RestAdapter.LogLevel.NONE)
//                .setConverter(gsonConverter)
//                .setClient(ditioOkClient)
                .build();
    }





    @Provides @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        return client;
    }

}
