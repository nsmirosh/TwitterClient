package miroshnychenko.mykola.twitterclient.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.adapters.TweetAdapter;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();


    @Inject
    TweetAdapter mTweetAdapter;

    @Inject
    PreferenceUtils mPreferenceUtils;

    @Bind(R.id.activity_main_user_timeline_rv)
    RecyclerView mTweetRV;

    @Bind(R.id.activity_main_srl)
    SwipeRefreshLayout mSwipeRefreshLayout;


    StatusesService mStatusesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((TwitterApplication) getApplication()).inject(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUserTimeLine();
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mTweetRV.setLayoutManager(layoutManager);

        if (mPreferenceUtils.hasTweets()) {
            setAdapterData(mPreferenceUtils.getTweets());
        }

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        mStatusesService = twitterApiClient.getStatusesService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserTimeLine();
//        if (mPreferenceUtils.getTweetBeans().size() > 0) {
//            registerReceiver()
//        }
    }

    public void updateUserTimeLine() {
        mStatusesService.userTimeline(null, null, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                setAdapterData(result.data);
                mPreferenceUtils.saveTweets(result.data);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            public void failure(TwitterException exception) {
                //Do something on failure
            }


        });

    }

    public void setAdapterData(List<Tweet> tweets) {
        mTweetAdapter.addAll(tweets);
        mTweetRV.setAdapter(mTweetAdapter);
    }


    @OnClick(R.id.list_fragment_create_income_fab)
    public void buildTweet() {
        Intent intent = new Intent(this, CreateTweetActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_main_log_out:
                mPreferenceUtils.deleteToken();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
