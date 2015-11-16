package miroshnychenko.mykola.twitterclient;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.twitter.sdk.android.core.models.Tweet;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.beans.TweetBean;
import miroshnychenko.mykola.twitterclient.models.AuthToken;
import miroshnychenko.mykola.twitterclient.modules.GsonFactory;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by nsmirosh on 11/16/2015.
 */
public class PreferenceUtilsTest {

    @Test
    public void testsaveAuthToken() {

        String token = "token";
        String secret = "secret";
        AuthToken authToken = new AuthToken(token, secret);
        PreferenceUtils preferenceUtils = new PreferenceUtils(getInstrumentation().getContext(), GsonFactory.getGson());

        //test
        preferenceUtils.saveAuthToken(authToken);
        AuthToken getAuthToken = preferenceUtils.getAuthToken();

        assertNotNull(getAuthToken);
        assertEquals(token, getAuthToken.getToken());
        assertEquals(secret, getAuthToken.getSecret());

    }

    @Test
    public void testHasToken() {

        String token = "token";
        String secret = "secret";
        AuthToken authToken = new AuthToken(token, secret);
        PreferenceUtils preferenceUtils = new PreferenceUtils(getInstrumentation().getContext(), GsonFactory.getGson());

        //test
        assertFalse(preferenceUtils.hasToken());
        preferenceUtils.saveAuthToken(authToken);
        assertTrue(preferenceUtils.hasToken());
    }

    @Test
    public void testDeleteToken() {

        String token = "token";
        String secret = "secret";
        AuthToken authToken = new AuthToken(token, secret);
        PreferenceUtils preferenceUtils = new PreferenceUtils(getInstrumentation().getContext(), GsonFactory.getGson());

        //test
        preferenceUtils.saveAuthToken(authToken);
        assertTrue(preferenceUtils.hasToken());
        preferenceUtils.deleteToken();
        assertFalse(preferenceUtils.hasToken());
    }

    @Test
    public void testSaveGetTweets() {

        List<Tweet> tweets = new ArrayList<>();
        tweets.add(new Tweet(null, null, null, null, null, false, null, 12l, null, null, 1l, null, 1l, null, null, null, false, null, 0, false, null, null, null, false, null, false, null , null));
        tweets.add(new Tweet(null, null, null, null, null, false, null, 100l, null, null, 1l, null, 1l, null, null, null, false, null, 0, false, null, null, null, false, null, false, null , null));
        String token = "token";
        AuthToken authToken = new AuthToken(token, null);
        PreferenceUtils preferenceUtils = new PreferenceUtils(getInstrumentation().getContext(), GsonFactory.getGson());

        //test
        preferenceUtils.saveAuthToken(authToken);
        preferenceUtils.saveTweets(tweets);
        assertTrue(preferenceUtils.hasTweets());
        assertEquals(12l, preferenceUtils.getTweets().get(0).getId());
        assertEquals(100l, preferenceUtils.getTweets().get(1).getId());
    }


    @Test
    public void testSaveTweetBean() {
        String token = "token";
        AuthToken authToken = new AuthToken(token, null);
        TweetBean tweetBean = new TweetBean("someText", "someImageUri");

        PreferenceUtils preferenceUtils = new PreferenceUtils(getInstrumentation().getContext(), GsonFactory.getGson());
        preferenceUtils.saveAuthToken(authToken);

        assertFalse(preferenceUtils.hasTweetBeans());
        preferenceUtils.saveTweetBean(tweetBean);

        assertTrue(preferenceUtils.hasTweetBeans());
        assertEquals("someText", preferenceUtils.getTweetBeans().get(0).getText());
        assertEquals("someImageUri", preferenceUtils.getTweetBeans().get(0).getImagePath());
    }


    @Test
    public void testSaveTweetBeans() {
        String token = "token";
        AuthToken authToken = new AuthToken(token, null);
        List<TweetBean> tweetBeans = new ArrayList<>();
        TweetBean tweetBean = new TweetBean("someText", "someImageUri");
        TweetBean tweetBean2 = new TweetBean("someMoreText", "someBiggerImageUri");
        tweetBeans.add(tweetBean);
        tweetBeans.add(tweetBean2);

        PreferenceUtils preferenceUtils = new PreferenceUtils(getInstrumentation().getContext(), GsonFactory.getGson());
        preferenceUtils.saveAuthToken(authToken);

        assertFalse(preferenceUtils.hasTweetBeans());
        preferenceUtils.saveTweetBeans(tweetBeans);

        assertTrue(preferenceUtils.hasTweetBeans());
        assertEquals("someText", preferenceUtils.getTweetBeans().get(0).getText());
        assertEquals("someImageUri", preferenceUtils.getTweetBeans().get(0).getImagePath());
        assertEquals("someMoreText", preferenceUtils.getTweetBeans().get(1).getText());
        assertEquals("someBiggerImageUri", preferenceUtils.getTweetBeans().get(1).getImagePath());
    }
}
