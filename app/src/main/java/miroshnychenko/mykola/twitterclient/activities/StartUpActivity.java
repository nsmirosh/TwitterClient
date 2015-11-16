package miroshnychenko.mykola.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.application.TwitterApplication;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

public class StartUpActivity extends AppCompatActivity {


    @Inject
    PreferenceUtils mPreferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ((TwitterApplication) getApplication()).inject(this);
        Intent intent;
        if (mPreferenceUtils.hasToken()) {
            intent  = new Intent(this, MainActivity.class);
        }
        else {
            intent = new Intent(this, LoginActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
