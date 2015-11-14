package miroshnychenko.mykola.twitterclient.services;

import android.app.IntentService;
import android.content.Intent;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.beans.TweetBean;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

/**
 * Created by nsmirosh on 11/14/2015.
 */
public class UploadTweetService extends IntentService {

    public static final String TWEETS_TO_UPLOAD = "tweets_to_upload";

    public static final String TAG = UploadTweetService.class.getSimpleName();

    StatusesService mStatusesService;

    List<TweetBean> mTweets;

    CallBack mCallBack;

    @Inject
    PreferenceUtils mPreferenceUtils;

    public UploadTweetService() {
        super("UploadTweetService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((TwitterApplication) getApplicationContext()).inject(this);
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        mStatusesService = twitterApiClient.getStatusesService();
        mCallBack = new CallBack();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mTweets = intent.getExtras().getParcelableArrayList(TWEETS_TO_UPLOAD);
        sendTweet(mTweets.size() - 1);

    }

    public class CallBack extends Callback<Tweet> {

        @Override
        public void success(Result<Tweet> result) {
            mTweets.remove(mTweets.size() - 1);
            if (mTweets.size() > 0) {
                sendTweet(mTweets.size() - 1);
            } else {
                stopSelf();
            }
        }

        @Override
        public void failure(TwitterException e) {
            mPreferenceUtils.saveTweetBeans(mTweets);
            stopSelf();
        }
    }

    public void sendTweet(int position) {
        mStatusesService.update(mTweets.get(position).getText(), null, null, null, null, null, null, null, null, mCallBack);
    }
}
