package miroshnychenko.mykola.twitterclient.tasks.base;



import miroshnychenko.mykola.twitterclient.events.BaseEvent;
import retrofit.RetrofitError;

/**
 * Created by vervik on 4/2/15.
 */
public abstract class BaseResultAsyncTask<T, Y> extends BaseAsyncTask<T, Y> {

    @Override
    @SafeVarargs
    //@SuppressWarnings("unchecked")
    final protected Y doInBackground(final T... params) {
        preDoInBackgroundHook(params);
        try {
            return getResult(params);
        } catch (RetrofitError e) {
            setException(e);
            return null;
        }
    }

    @Override
    final protected void onPostExecute(Y result) {
        super.onPostExecute(result);
        mBus.post(createBaseEvent(result, getException()));
    }

    @SuppressWarnings("unchecked")
    protected abstract Y getResult(T... params) throws RetrofitError;

    protected abstract BaseEvent<Y> createBaseEvent(Y result, RetrofitError exception);

}
