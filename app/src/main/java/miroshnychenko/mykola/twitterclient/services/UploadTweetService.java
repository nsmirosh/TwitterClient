package miroshnychenko.mykola.twitterclient.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.MediaService;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.beans.TweetBean;
import miroshnychenko.mykola.twitterclient.utils.MediaUtils;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

/**
 * Created by nsmirosh on 11/14/2015.
 */
public class UploadTweetService extends IntentService {

    public static final String TWEETS_TO_UPLOAD = "tweets_to_upload";

    public static final String TAG = UploadTweetService.class.getSimpleName();

    StatusesService mStatusesService;
    MediaService mMediaService;

    String mMediaString;
    String mTweetText;

    List<TweetBean> mTweets;

    TweetCallBack mTweetCallBack;
    MediaCallBack mMediaCallBack;

    @Inject
    PreferenceUtils mPreferenceUtils;


    @Inject
    MediaUtils mMediaUtils;

    public UploadTweetService() {
        super("UploadTweetService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((TwitterApplication) getApplicationContext()).inject(this);
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        mStatusesService = twitterApiClient.getStatusesService();
        mMediaService = twitterApiClient.getMediaService();
        mTweetCallBack = new TweetCallBack();
        mMediaCallBack = new MediaCallBack();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mTweets = intent.getExtras().getParcelableArrayList(TWEETS_TO_UPLOAD);
        sendTweet(mTweets.size() - 1);
    }

    public void sendTweet(int position) {
        TweetBean tweet = mTweets.get(position);
        mTweetText = tweet.getText();
        if (tweet.getImagePath()  != null) {
            mMediaService.upload(mMediaUtils.getFile(Uri.parse(tweet.getImagePath())), null, null, mMediaCallBack);
        }
        else {
            uploadTweet();
            mMediaString = null;
        }

    }

    public class TweetCallBack extends Callback<Tweet> {

        @Override
        public void success(Result<Tweet> result) {
            mTweets.remove(mTweets.size() - 1);
            if (mTweets.size() > 0) {
                sendTweet(mTweets.size() - 1);
            } else {
                saveAndStop();
            }
        }

        @Override
        public void failure(TwitterException e) {
            saveAndStop();
        }

    }


    public class MediaCallBack extends Callback<Media> {

        @Override
        public void success(Result<Media> result) {
            mMediaString = result.data.mediaIdString;
            uploadTweet();
        }

        @Override
        public void failure(TwitterException e) {
            saveAndStop();
        }
    }


    public void uploadTweet() {
        mStatusesService.update(mTweetText, null, null, null, null, null, null, null, mMediaString, mTweetCallBack);
    }

    public void saveAndStop() {
        mPreferenceUtils.saveTweetBeans(mTweets);
        stopSelf();
    }
}
