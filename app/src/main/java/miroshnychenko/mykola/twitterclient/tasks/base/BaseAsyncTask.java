package miroshnychenko.mykola.twitterclient.tasks.base;

import android.os.AsyncTask;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.listeners.AsyncCallbackListener;
import retrofit.RetrofitError;

/**
 * Created by Thomas on 7/14/14.
 */
public abstract class BaseAsyncTask <T, Y> extends AsyncTask<T, Void, Y> {

    protected String TAG = getClass().getSimpleName();

    @Inject
    protected AsyncCallbackListener mListener;
    @Inject
    protected Bus mBus;

    protected RetrofitError exception;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onShowProgressBar();
    }

    @SuppressWarnings("unchecked")
    protected void preDoInBackgroundHook(final T... params) {
        // empty implementation such that sub classes can override
    }

    @Override
    protected void onPostExecute(Y y) {
        super.onPostExecute(y);
        if(mListener != null) {
            mListener.dismissProgressDialog();
        }
    }

    public void setListener(AsyncCallbackListener mListener) {
        this.mListener = mListener;
    }

    public boolean isSuccess() {
        return getHttpStatus() == 200;
    }

    public int getHttpStatus() {
        return exception != null && exception.getResponse() != null ?  exception.getResponse().getStatus() : 200;
    }

    public RetrofitError getException() {
        return exception;
    }

    public void setException(RetrofitError exception) {
        this.exception = exception;
    }
}