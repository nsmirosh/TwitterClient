package miroshnychenko.mykola.twitterclient;

import android.app.Application;

import com.fizzbuzz.android.dagger.InjectingApplication;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import miroshnychenko.mykola.twitterclient.modules.TwitterContextModule;

/**
 * Created by nsmirosh on 11/10/2015.
 */
public class TwitterApplication extends InjectingApplication {

    private static final String TWITTER_KEY = "4DJ5ieeFtGuVOCaHQbwarwoPQ";
    private static final String TWITTER_SECRET = "vIGfIDbb4R8oxGRLkqthH4Kn1Hh1K8MZr4ub9F7a7gukTBof0s";


    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TwitterCore(authConfig), new TweetComposer());
    }

    @Override
    protected List<Object> getModules() {
        List<Object>  list = super.getModules();
        list.add(new TwitterContextModule(this));
        return list;
    }
}
