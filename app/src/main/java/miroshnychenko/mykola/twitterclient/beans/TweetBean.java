package miroshnychenko.mykola.twitterclient.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nsmirosh on 11/14/2015.
 */
public class TweetBean implements Parcelable{

    String text;

    public TweetBean(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);

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
    }
}
