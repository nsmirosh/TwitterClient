package miroshnychenko.mykola.twitterclient.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.fizzbuzz.android.dagger.InjectingActivityModule;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Module;
import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.activities.base.BaseAsyncFragmentActivity;
import miroshnychenko.mykola.twitterclient.helper.SessionIdentifierGenerator;
import miroshnychenko.mykola.twitterclient.models.Login;
import miroshnychenko.mykola.twitterclient.modules.TwitterContextModule;
import miroshnychenko.mykola.twitterclient.tasks.LoginTask;

public class LoginActivity extends BaseAsyncFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.activity_login_enter_btn)
    public void login (){
        Login login  = new Login();
        login.setConsumerKey("4DJ5ieeFtGuVOCaHQbwarwoPQ");
        login.setCallback("http://balls.com");
        login.setNonce(new SessionIdentifierGenerator().nextSessionId());
        login.setTimestamp(System.currentTimeMillis());
        mTaskFragment.loginTask(login);

    }


    @Subscribe
    public void onLogin(LoginTask.LoginEvent event) {
        if (event.isSuccess()) {
            Toast.makeText(this, "Hurray", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "shit", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected Object getModule() {
        return new LoginActivityModule();
    }

    @Module(
            injects = {LoginActivity.class},
            addsTo = TwitterContextModule.class,
            includes = InjectingActivityModule.class
    )

    public static class LoginActivityModule {

    }
}
