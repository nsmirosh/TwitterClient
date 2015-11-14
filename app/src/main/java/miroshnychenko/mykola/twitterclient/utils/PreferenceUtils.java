package miroshnychenko.mykola.twitterclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twitter.sdk.android.core.models.Tweet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.beans.TweetBean;
import miroshnychenko.mykola.twitterclient.models.AuthToken;

/**
 * Created by nsmirosh on 11/10/2015.
 */
public class PreferenceUtils {

    public static final String PREFS_TOKEN_KEY = "prefs_token_key";
    public static final String PREFS_TWEETS_KEY = "prefs_tweets_key";
    public static final String PREFS_TWEETS_BEAN_KEY = "prefs_tweets_bean_key";

    private SharedPreferences mPrefs;

    public static final String TAG = PreferenceUtils.class.getSimpleName();
    Gson mGson;

    @Inject
    public PreferenceUtils(Context context, Gson gson) {
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mGson = gson;
    }

    public void clearAll() {
        mPrefs.edit().clear().apply();
    }

    public void saveAuthToken(AuthToken authToken) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREFS_TOKEN_KEY, mGson.toJson(authToken));
        editor.apply();
    }

    public AuthToken getAuthToken() {
        return mGson.fromJson(mPrefs.getString(PREFS_TOKEN_KEY, ""), AuthToken.class);
    }
    public void deleteToken() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(PREFS_TOKEN_KEY);
        editor.apply();
    }

    public boolean hasToken() {
        return getAuthToken() != null;
    }

    public void saveTweets(List<Tweet> tweets) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREFS_TWEETS_KEY + getAuthToken().getToken(), mGson.toJson(tweets));
        editor.apply();
    }

    public List<Tweet> getTweets() {
        Type type = new TypeToken<List<Tweet>>(){}.getType();
        return mGson.fromJson(mPrefs.getString(PREFS_TWEETS_KEY + getAuthToken().getToken(), null), type);
    }

    public boolean hasTweets() {
        return mPrefs.contains(PREFS_TWEETS_KEY + getAuthToken().getToken());
    }

    public void saveTweetBean(TweetBean tweetBean) {
        List<TweetBean> tweetBeans;
        if (getTweetBeans() != null) {
            tweetBeans = getTweetBeans();
            tweetBeans.add(0, tweetBean);
        }
        else {
            tweetBeans = new ArrayList<>();
            tweetBeans.add(tweetBean);
        }
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREFS_TWEETS_BEAN_KEY + getAuthToken().getToken(), mGson.toJson(tweetBeans));
        editor.apply();
    }

    public void saveTweetBeans(List<TweetBean> tweetBeans) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREFS_TWEETS_BEAN_KEY + getAuthToken().getToken(), mGson.toJson(tweetBeans));
        editor.apply();
    }

    public List<TweetBean> getTweetBeans() {
        Type type = new TypeToken<List<TweetBean>>(){}.getType();
        return mGson.fromJson(mPrefs.getString(PREFS_TWEETS_BEAN_KEY + getAuthToken().getToken(), null), type);
    }

    public boolean hasTweetBeans() {
        return mPrefs.contains(PREFS_TWEETS_BEAN_KEY + getAuthToken().getToken());
    }
}
