package miroshnychenko.mykola.twitterclient.utils;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Created by vervik on 5/24/15.
 */
public class PicassoUtils {

    @Inject
    Picasso mPicasso;

    public void loadThumbnailImage(String url, ImageView imageView) {
        if (url != null) {
            mPicasso.load(url)
                    .resize(300, 300)
                    .centerCrop()
                    .into(imageView);
        }
    }
}
