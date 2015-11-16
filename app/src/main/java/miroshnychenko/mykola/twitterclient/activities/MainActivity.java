package miroshnychenko.mykola.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.application.TwitterApplication;
import miroshnychenko.mykola.twitterclient.activities.base.BaseProgressActivity;
import miroshnychenko.mykola.twitterclient.adapters.TweetAdapter;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

public class MainActivity extends BaseProgressActivity {

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
        } else {
            showProgressDialog();
        }

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        mStatusesService = twitterApiClient.getStatusesService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserTimeLine();

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
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
        dismissProgressDialog();

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public String getProgressString() {
        return getString(R.string.activity_main_downloading_tweets);
    }
}
