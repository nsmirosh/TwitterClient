package miroshnychenko.mykola.twitterclient.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

import javax.inject.Inject;

import retrofit.mime.TypedFile;

/**
 * Created by nsmirosh on 11/15/2015.
 */
public class MediaUtils {

    @Inject
    Context mContext;

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = mContext.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Bitmap getBitmap(Uri imageUri) {
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getRealPathFromURI(imageUri)), 300, 300);
    }


    public TypedFile getFile(Uri imageUri) {
        File photo = new File(getRealPathFromURI(imageUri));
        return new TypedFile("application/octet-stream", photo);
    }

}
