package miroshnychenko.mykola.twitterclient.activities;

import android.os.Bundle;

import com.fizzbuzz.android.dagger.InjectingActivityModule;

import dagger.Module;
import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.activities.base.BaseAsyncFragmentActivity;
import miroshnychenko.mykola.twitterclient.modules.TwitterContextModule;

public class MainActivity extends BaseAsyncFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //module
    @Override
    protected Object getModule() {
        return new FeedActivityModule();
    }

    @Module(
            injects = {MainActivity.class},
            addsTo = TwitterContextModule.class,
            includes = InjectingActivityModule.class
    )

    public static class FeedActivityModule {

    }
}
