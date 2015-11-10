package miroshnychenko.mykola.twitterclient.modules;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import miroshnychenko.mykola.twitterclient.http.RestAPI;
import miroshnychenko.mykola.twitterclient.misc.AndroidBus;
import retrofit.converter.GsonConverter;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by nsmirosh on 7/8/2015.
 */

@Module(
        complete = false,
        library = true
)
public class DitioModule {

    @Provides
    @Singleton
    GsonConverter provideGsonConverter(Gson gson) {
        return new GsonConverter(gson);
    }

    @Provides @Singleton
    Gson provideGson() {
        return GsonFactory.getGson();
    }

    @Provides
    @UrlBase
    String provideUrlBase() {
        return RestAPI.URLBASE;
    }


    @Provides
    @Singleton
    Bus provideBus() {
        return new AndroidBus(ThreadEnforcer.ANY);
    }

    @Qualifier
    @Target({FIELD, PARAMETER, METHOD})
    @Documented
    @Retention(RUNTIME)
    public @interface UrlBase {
    }
}
