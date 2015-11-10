package miroshnychenko.mykola.twitterclient.modules;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;

import com.squareup.okhttp.OkHttpClient;


import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import dagger.Module;
import dagger.Provides;
import miroshnychenko.mykola.twitterclient.BuildConfig;
import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.http.LoginApi;
import retrofit.Endpoint;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by nsmirosh on 7/8/2015.
 */

@Module(
        complete = false,
        library = true,
        includes = DitioModule.class,
        injects = {
                TwitterApplication.class
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

    @Provides
    ContentResolver provideContentResolver() {
        return mContext.getContentResolver();
    }

    @Provides
    LoginApi provideLoginApi(RestAdapter restAdapter) {
        return restAdapter
                .create(LoginApi.class);
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


    @Provides
    Endpoint provideEndpoint(@DitioModule.UrlBase final String baseUrl) {
        return new Endpoint() {
            @Override
            public String getUrl() {
                return baseUrl;
            }

            @Override
            public String getName() {
                return "default";
            }
        };
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
