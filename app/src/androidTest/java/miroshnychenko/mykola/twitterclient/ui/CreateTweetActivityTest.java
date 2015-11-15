package miroshnychenko.mykola.twitterclient.ui;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.activities.CreateTweetActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateTweetActivityTest extends ActivityInstrumentationTestCase2<CreateTweetActivity> {


    public static final String MORE_THAN_140_CHARACTERS = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    public CreateTweetActivityTest() {
        super(CreateTweetActivity.class);
    }

    @Rule
    public ActivityTestRule<CreateTweetActivity> mActivityRule = new ActivityTestRule(CreateTweetActivity.class);

    @Test
    public void emptyTweet_test() {
        onView(withId(R.id.activity_create_tweet_et)).perform(typeText(""));
        onView(withId(R.id.activity_create_tweet_done_btn)).perform(click());
        onView(withId(R.id.activity_create_tweet_et)).check(matches(withError(
                "You have to tweet some text")));
    }

    @Test
    public void moreThan140Characters_test() {
        onView(withId(R.id.activity_create_tweet_et)).perform(typeText(MORE_THAN_140_CHARACTERS), closeSoftKeyboard());
        onView(withId(R.id.activity_create_tweet_done_btn)).perform(click());
        onView(withId(R.id.activity_create_tweet_et)).check(matches(withError(
                "The tweet has to be less than 140 characters")));
    }

    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return editText.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }


}