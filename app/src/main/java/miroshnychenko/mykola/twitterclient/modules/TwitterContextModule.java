package miroshnychenko.mykola.twitterclient.modules;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.activities.CreateTweetActivity;
import miroshnychenko.mykola.twitterclient.activities.MainActivity;
import miroshnychenko.mykola.twitterclient.activities.StartUpActivity;
import miroshnychenko.mykola.twitterclient.receivers.NetworkChangeReceiver;
import miroshnychenko.mykola.twitterclient.services.UploadTweetService;
import miroshnychenko.mykola.twitterclient.utils.PicassoUtils;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;
import retrofit.converter.GsonConverter;

/**
 * Created by nsmirosh on 7/8/2015.
 */

@Module(
        complete = false,
        library = true,
        injects = {
                TwitterApplication.class, StartUpActivity.class, PreferenceUtils.class, MainActivity.class,
                CreateTweetActivity.class, NetworkChangeReceiver.class, UploadTweetService.class, PicassoUtils.class
        }
)
public class TwitterContextModule {

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
    Picasso providePicasso(Context context) {
        Picasso.Builder builder = new Picasso.Builder(context);
        return builder.build();
    }

}
