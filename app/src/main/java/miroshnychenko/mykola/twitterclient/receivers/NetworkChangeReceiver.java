package miroshnychenko.mykola.twitterclient.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.application.TwitterApplication;
import miroshnychenko.mykola.twitterclient.beans.TweetBean;
import miroshnychenko.mykola.twitterclient.services.UploadTweetService;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {


    @Inject
    PreferenceUtils mPreferenceUtils;


    public static final String TAG = NetworkChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        ((TwitterApplication) context.getApplicationContext()).inject(this);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (mPreferenceUtils.getTweetBeans().size() > 0) {
                Intent serviceIntent = new Intent(context, UploadTweetService.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(UploadTweetService.TWEETS_TO_UPLOAD, (ArrayList<TweetBean>) mPreferenceUtils.getTweetBeans());
                serviceIntent.putExtras(bundle);
                context.startService(serviceIntent);
            }

        } else {
            Log.d(TAG, "Internet not available");

        }
    }
}