package miroshnychenko.mykola.twitterclient.activities;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

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
import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.beans.TweetBean;
import miroshnychenko.mykola.twitterclient.receivers.NetworkChangeReceiver;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

public class CreateTweetActivity extends AppCompatActivity {


    public static final String TAG = CreateTweetActivity.class.getSimpleName();

    @Bind(R.id.activity_create_tweet_et)
    EditText mTextET;

    @Inject
    PreferenceUtils mPreferenceUtils;

    StatusesService mStatusesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tweet);
        ButterKnife.bind(this);
        ((TwitterApplication) getApplication()).inject(this);
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        mStatusesService = twitterApiClient.getStatusesService();
    }

    @OnClick(R.id.activity_create_tweet_done_btn)
    public void sendTweet() {
        mStatusesService.update(mTextET.getText().toString(), null, null, null, null, null, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
            }

            public void failure(TwitterException exception) {
                mPreferenceUtils.saveTweetBean(new TweetBean(mTextET.getText().toString()));
            }
        });
        finish();
    }
}
