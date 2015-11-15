package miroshnychenko.mykola.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.models.AuthToken;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity {


    public static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    PreferenceUtils mPreferenceUtils;

    @Bind(R.id.activity_login_twitter_button)
    TwitterLoginButton mTwitterLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TwitterApplication) getApplication()).inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mTwitterLoginBtn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                TwitterAuthToken TwitterToken = result.data.getAuthToken();
                mPreferenceUtils.saveAuthToken(new AuthToken(TwitterToken.token, TwitterToken.secret));

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        mTwitterLoginBtn.onActivityResult(requestCode, resultCode, data);
    }
}
