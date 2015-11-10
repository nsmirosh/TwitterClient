package miroshnychenko.mykola.twitterclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.models.Token;

/**
 * Created by nsmirosh on 11/10/2015.
 */
public class PreferenceUtils {

    public static final String PREFS_KEY_TOKEN = "prefsToken";

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

    public void saveToken(Token token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREFS_KEY_TOKEN, mGson.toJson(token));
        editor.apply();
    }

    public Token getToken() {
        return mGson.fromJson(mPrefs.getString(PREFS_KEY_TOKEN, ""), Token.class);
    }

    public boolean hasToken() {
        return getToken() != null;
    }

}
