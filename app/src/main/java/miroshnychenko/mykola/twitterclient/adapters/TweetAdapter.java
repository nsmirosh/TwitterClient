package miroshnychenko.mykola.twitterclient.adapters;

import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.R;
import miroshnychenko.mykola.twitterclient.helpers.TwitterDateHelper;
import miroshnychenko.mykola.twitterclient.utils.PicassoUtils;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    public List<Tweet> mItems;


    @Inject
    PicassoUtils mPicassoUtils;

    @Inject
    public TweetAdapter () {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextTV;
        public TextView mCreatedTimeTV;
        public ImageView mImageIV;
        public ViewHolder(View v) {
            super(v);
            mCreatedTimeTV = (TextView) v.findViewById(R.id.adapter_tweet_create_time_tv);
            mTextTV = (TextView) v.findViewById(R.id.adapter_tweet_row_text_tv);
            mImageIV = (ImageView) v.findViewById(R.id.adapter_tweet_row_image_iv);
        }
    }


    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tweet_row, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Tweet tweet = mItems.get(position);

        holder.mCreatedTimeTV.setText(DateUtils.getRelativeTimeSpanString(TwitterDateHelper.parseDate(tweet.createdAt), System.currentTimeMillis(), 0));

        holder.mTextTV.setText(tweet.text);
        if (tweet.entities.media != null) {
            holder.mImageIV.setVisibility(View.VISIBLE);
            mPicassoUtils.loadThumbnailImage(tweet.entities.media.get(0).mediaUrl, holder.mImageIV);
        }
        else {
            holder.mImageIV.setVisibility(View.GONE);
        }

        holder.mTextTV.setTag(holder);
        holder.mImageIV.setTag(holder);


    }

    public void addAll(List<Tweet> tweets) {
        mItems = tweets;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


}