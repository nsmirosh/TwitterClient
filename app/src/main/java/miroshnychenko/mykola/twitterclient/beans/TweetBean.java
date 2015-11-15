package miroshnychenko.mykola.twitterclient.beans;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nsmirosh on 11/14/2015.
 */
public class TweetBean implements Parcelable{

    String text;
    String imageUri;

    public TweetBean() {

    }

    public TweetBean(String text, String imageUri) {
        this.text = text;
        this.imageUri = imageUri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imageUri;
    }

    public void setImagePath(String imagePath) {
        this.imageUri = imagePath;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(imageUri);

    }

    public static final Parcelable.Creator<TweetBean> CREATOR
            = new Parcelable.Creator<TweetBean>() {
        public TweetBean createFromParcel(Parcel in) {
            return new TweetBean(in);
        }

        public TweetBean[] newArray(int size) {
            return new TweetBean[size];
        }
    };

    private TweetBean(Parcel in) {
        text = in.readString();
        imageUri = in.readString();
    }
}
