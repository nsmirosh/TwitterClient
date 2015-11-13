package miroshnychenko.mykola.twitterclient.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import javax.inject.Inject;

import miroshnychenko.mykola.twitterclient.R;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    public List<Tweet> mItems;

    @Inject
    public TweetAdapter () {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextTV;
        public ViewHolder(View v) {
            super(v);
            mTextTV = (TextView) v.findViewById(R.id.adapter_tweet_row_text_tv);
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

        final ViewHolder vh = holder;
        vh.mTextTV.setText(tweet.text);
        holder.mTextTV.setTag(holder);


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