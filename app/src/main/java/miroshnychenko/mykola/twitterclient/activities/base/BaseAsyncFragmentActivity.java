package miroshnychenko.mykola.twitterclient.activities.base;

import android.app.ProgressDialog;
import android.os.Bundle;


import com.fizzbuzz.android.dagger.InjectingFragmentActivity;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.fragments.AsyncBackgroundFragment;
import miroshnychenko.mykola.twitterclient.listeners.AsyncCallbackListener;

/**
 * Created by Thomas on 6/30/14.
 */
public abstract class BaseAsyncFragmentActivity extends InjectingFragmentActivity implements AsyncCallbackListener {

    public static final String BACKGROUND_FRAGMENT_TAG = "background_fragment_tag";

    @Inject
    Bus mBus;


    protected AsyncBackgroundFragment mTaskFragment;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskFragment = (AsyncBackgroundFragment) getSupportFragmentManager().findFragmentByTag(BACKGROUND_FRAGMENT_TAG);
        // If the Fragment is non-null, then it is currently being retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new AsyncBackgroundFragment();
            getSupportFragmentManager().beginTransaction().add(mTaskFragment, BACKGROUND_FRAGMENT_TAG).commitAllowingStateLoss();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBus.unregister(this);
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onShowProgressBar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getProgressString());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public void onSuccess(Object... args) {
        dismissProgressDialog();
    }


    public void onFailure(Exception exception) {
        dismissProgressDialog();
    }

    public String getProgressString() {
        return getString(R.string.dialog_progress_generic);
    }

    @Override
    protected List<Object> getModules() {
        List<Object> list = super.getModules();
        list.add(getModule());

        return list;
    }

    protected abstract Object getModule();
}
