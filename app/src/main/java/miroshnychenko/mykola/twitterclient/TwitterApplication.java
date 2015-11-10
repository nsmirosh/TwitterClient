package miroshnychenko.mykola.twitterclient;

import android.app.Application;

import com.fizzbuzz.android.dagger.InjectingApplication;

import java.util.List;

import miroshnychenko.mykola.twitterclient.modules.TwitterContextModule;

/**
 * Created by nsmirosh on 11/10/2015.
 */
public class TwitterApplication extends InjectingApplication {

    @Override
    protected List<Object> getModules() {
        List<Object>  list = super.getModules();
        list.add(new TwitterContextModule(this));
        return list;
    }
}
