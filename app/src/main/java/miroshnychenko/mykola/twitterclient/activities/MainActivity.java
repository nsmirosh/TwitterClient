package miroshnychenko.mykola.twitterclient.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.fizzbuzz.android.dagger.InjectingActivityModule;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Module;
import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.activities.base.BaseAsyncFragmentActivity;
import miroshnychenko.mykola.twitterclient.adapters.TweetAdapter;
import miroshnychenko.mykola.twitterclient.modules.TwitterContextModule;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();


    @Inject
    TweetAdapter mTweetAdapter;

    @Bind(R.id.activity_main_user_timeline_lv)
    ListView mTweetLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((TwitterApplication) getApplication()).inject(this);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        mTweetLV.setLayoutManager(layoutManager);


        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();


        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(null)
                .build();


        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        mTweetLV.setAdapter(adapter);

//        statusesService.userTimeline(null, null, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {
//            @Override
//            public void success(Result<List<Tweet>> result) {
//
////                mTweetAdapter.addAll(result.data);
////                mTweetRV.setAdapter(mTweetAdapter);
//
//            }
//
//            public void failure(TwitterException exception) {
//                //Do something on failure
//            }
//        });
    }

    public void setUpTimeLine() {

    }


    @OnClick(R.id.list_fragment_create_income_fab)
    public void buildTweet() {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("just setting up my Fabric.")
                .image(Uri.fromFile(new File("/sdcard/cats.jpg")));
        builder.show();
    }


//    //module
//    @Override
//    protected Object getModule() {
//        return new FeedActivityModule();
//    }
//
//    @Module(
//            injects = {MainActivity.class},
//            addsTo = TwitterContextModule.class,
//            includes = InjectingActivityModule.class
//    )
//
//    public static class FeedActivityModule {
//
//    }
}
