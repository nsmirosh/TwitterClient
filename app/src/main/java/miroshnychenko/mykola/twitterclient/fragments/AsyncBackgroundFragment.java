package miroshnychenko.mykola.twitterclient.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.fizzbuzz.android.dagger.InjectingActivityModule;
import com.fizzbuzz.android.dagger.InjectingFragment;
import com.fizzbuzz.android.dagger.InjectingFragmentModule;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import miroshnychenko.mykola.twitterclient.listeners.AsyncCallbackListener;
import miroshnychenko.mykola.twitterclient.models.Login;
import miroshnychenko.mykola.twitterclient.tasks.LoginTask;
import miroshnychenko.mykola.twitterclient.tasks.base.BaseAsyncTask;

/**
 *
 */
public class AsyncBackgroundFragment extends InjectingFragment {

    private final String TAG = AsyncBackgroundFragment.class.getSimpleName();

    public static final String BACKGROUND_FRAGMENT_TAG = "background";

    LoginTask mLoginTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach");
        super.onAttach(activity);
        AsyncCallbackListener listener = (AsyncCallbackListener) activity;
        attachListener(mLoginTask, listener);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
        unattachListener(mLoginTask);

    }


    // ASYNC TASKS
    public void loginTask(Login login) {
        if (!existAndOrRunning(mLoginTask)) {
            mLoginTask = getObjectGraph().get(LoginTask.class);
            mLoginTask.execute(login);
        }
    }




    private void attachListener(BaseAsyncTask task, AsyncCallbackListener mListener) {
        if(task != null) {
            task.setListener(mListener);
            if(task.getStatus() != AsyncTask.Status.FINISHED) {
                mListener.onShowProgressBar();
            }
        }
    }

    private void unattachListener(BaseAsyncTask task) {
        if(task != null) {
            task.setListener(null);
        }
    }

    private boolean existAndOrRunning(BaseAsyncTask task) {
        if(task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            Log.d(TAG, task.getClass() + ": Task running, but not yet completed");
            return true;
        }
        return false;
    }


    public static AsyncBackgroundFragment newInstance() {
        return new AsyncBackgroundFragment();
    }

    @Module(
            library = false,
            complete = false,
            addsTo = InjectingActivityModule.class,
            includes = InjectingFragmentModule.class,
            injects = {
                    AsyncBackgroundFragment.class, LoginTask.class
            }
    )
    public class AsyncBackgroundFragmentModule {

        @Provides
        @Singleton
        public AsyncCallbackListener provideAsyncCallbackListener(Activity activity) {
            return (AsyncCallbackListener) activity;
        }
    }

    @Override
    protected List<Object> getModules() {
        List<Object> list = super.getModules();
        list.add(new AsyncBackgroundFragmentModule());
        return list;
    }
}
