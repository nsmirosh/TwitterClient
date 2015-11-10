package miroshnychenko.mykola.twitterclient.events;

import retrofit.RetrofitError;

/**
 * Created by vervik on 4/2/15.
 */
public abstract class BaseEventRoot {

    protected RetrofitError exception;

    public BaseEventRoot(RetrofitError exception) {
        this.exception = exception;
    }

    public RetrofitError getException() {
        return exception;
    }

    public boolean is400() {
        return exception != null && exception.getResponse() != null && exception.getResponse().getStatus() == 400;
    }

    public boolean is401() {
        return exception != null && exception.getResponse() != null && exception.getResponse().getStatus() == 401;
    }

    public boolean is403() {
        return exception != null && exception.getResponse() != null && exception.getResponse().getStatus() == 403;
    }

    public boolean is404() {
        return exception != null && exception.getResponse() != null && exception.getResponse().getStatus() == 404;
    }

    /**
     * Represents time out
     * @return
     */
    public boolean is408() {
        return exception != null && exception.getResponse() != null && exception.getResponse().getStatus() == 408;
    }

    public int getStatus() {
        return exception != null && exception.getResponse() != null ?  exception.getResponse().getStatus() : 0;
    }


    public boolean hasError() {
        return exception != null;
    }

    abstract boolean isSuccess();
}
