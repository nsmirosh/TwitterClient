package miroshnychenko.mykola.twitterclient.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.MediaService;
import com.twitter.sdk.android.core.services.StatusesService;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.TwitterApplication;
import miroshnychenko.mykola.twitterclient.beans.TweetBean;
import miroshnychenko.mykola.twitterclient.utils.MediaUtils;
import miroshnychenko.mykola.twitterclient.utils.PreferenceUtils;

public class CreateTweetActivity extends AppCompatActivity {


    public static final String TAG = CreateTweetActivity.class.getSimpleName();

    private static final int CAMERA_PIC_REQUEST = 1;

    @Bind(R.id.activity_create_tweet_et)
    EditText mTextET;

    @Bind(R.id.activity_create_tweet_image_preview_iv)
    ImageView mPreviewIV;

    @Inject
    PreferenceUtils mPreferenceUtils;

    @Inject
    MediaUtils mMediaUtils;

    StatusesService mStatusesService;
    MediaService mMediaService;
    TwitterApiClient mTwitterApiClient;
    Uri mImageUri;
    String mMediaString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tweet);
        ButterKnife.bind(this);
        ((TwitterApplication) getApplication()).inject(this);
        mTwitterApiClient = TwitterCore.getInstance().getApiClient();
        mStatusesService = mTwitterApiClient.getStatusesService();
        mMediaService = mTwitterApiClient.getMediaService();

    }

    @OnClick(R.id.activity_create_tweet_image_preview_iv)
    public void takePhoto() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_PIC_REQUEST:
                    if (intent != null) {
                        mImageUri = intent.getData();
                    }

                    Bitmap ThumbImage = mMediaUtils.getBitmap(mImageUri);
                    mPreviewIV.setImageBitmap(ThumbImage);
            }
        } else {
            Toast toast = Toast.makeText(this, "No Image is selected.",
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void uploadImage() {

        mMediaService.upload(mMediaUtils.getFile(mImageUri), null, null, new Callback<Media>() {
            @Override
            public void success(Result<Media> mediaResult) {

                mMediaString = mediaResult.data.mediaIdString;
                uploadTweet();
            }

            public void failure(TwitterException exception) {
                saveTweet();
                finish();
                Toast toast = Toast.makeText(getApplicationContext(), "Your tweet is saved and will be posted when internet is available.",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }


    public void uploadTweet() {
        mStatusesService.update(mTextET.getText().toString(), null, null, null, null, null, null, null, mMediaString, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
            }

            public void failure(TwitterException exception) {
                saveTweet();
                Toast toast = Toast.makeText(getApplicationContext(), "Your tweet is saved and will be posted when internet is available.",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
        finish();
        setProgressBarIndeterminateVisibility(false);
    }

    @OnClick(R.id.activity_create_tweet_done_btn)
    public void sendTweet() {
        if (TextUtils.isEmpty(mTextET.getText().toString())) {
            mTextET.setError(getString(R.string.activity_create_tweet_enter_text_error));
            return;
        }
        else if (mTextET.getText().toString().length() > 140) {
            mTextET.setError(getString(R.string.activity_create_tweet_too_many_characters_error));
            return;
        }
        setProgressBarIndeterminateVisibility(true);
        if (mImageUri != null) {
            uploadImage();
        } else {
            uploadTweet();
        }
    }

    public void saveTweet() {
        TweetBean tweetBean = new TweetBean();
        tweetBean.setText(mTextET.getText().toString());
        if (mImageUri != null) {
            tweetBean.setImagePath(String.valueOf(mImageUri));
        }
        mPreferenceUtils.saveTweetBean(tweetBean);
    }

}
