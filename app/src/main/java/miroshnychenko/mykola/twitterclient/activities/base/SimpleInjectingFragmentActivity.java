package miroshnychenko.mykola.twitterclient.activities.base;

import android.app.ProgressDialog;

import android.os.Bundle;

import com.fizzbuzz.android.dagger.InjectingFragmentActivity;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.fragments.AsyncBackgroundFragment;
import miroshnychenko.mykola.twitterclient.listeners.AsyncCallbackListener;


/**
 *
 */
public abstract class SimpleInjectingFragmentActivity extends InjectingFragmentActivity implements AsyncCallbackListener {

    @Inject
    protected Bus mBus;

    protected AsyncBackgroundFragment mTaskFragment;
    private ProgressDialog mProgressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskFragment = (AsyncBackgroundFragment) getSupportFragmentManager().findFragmentByTag(AsyncBackgroundFragment.BACKGROUND_FRAGMENT_TAG);
        // If the Fragment is non-null, then it is currently being retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = AsyncBackgroundFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(mTaskFragment, AsyncBackgroundFragment.BACKGROUND_FRAGMENT_TAG).commit();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }



    protected Object getModule() {
        return null;
    }

    @Override
    protected List<Object> getModules() {
        List<Object> list = super.getModules();
        if (getModule() != null) {
            list.add(getModule());
        }
        return list;
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

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public String getProgressString() {
        return getString(R.string.dialog_progress_generic);
    }
}
