package miroshnychenko.mykola.twitterclient.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class GsonFactory {

    private static Gson gson;

    public static Gson getGson() {
        if(gson == null) {
            gson = createGson();
        }
        return gson;
    }


    private static Gson createGson() {
        return new GsonBuilder()
                .create();
    }
}
