package miroshnychenko.mykola.twitterclient.events;

import retrofit.RetrofitError;

/**
 * Created by Thomas on 7/20/14.
 */
public abstract class BaseEvent<T> extends BaseEventRoot {

    private T result;

    public BaseEvent(T result, RetrofitError exception) {
        super(exception);
        this.result = result;
    }

    @Override
    public boolean isSuccess() {
        return getResult() != null;
    }

    public T getResult() {
        return result;
    }

}