package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private final int REQUEST_CODE = 20;
    public static List<Tweet> mTweets;
    public static Context context;
    TwitterClient client;
    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;

    }
    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        client = TwitterApp.getRestClient(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);

        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvScreenName.setText("@" + tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(getRelativeTimeAgo(tweet.createdAt));

        Glide.with(context).load(tweet.user.profileImageUrl).bitmapTransform(new CropCircleTransformation(context)).into(holder.ivProfileImage);

        if (tweet.media) {
            holder.ivMedia.setVisibility(View.VISIBLE);
            Glide.with(context).load(tweet.media_url).bitmapTransform(new RoundedCornersTransformation(context, 10, 0)).into(holder.ivMedia);
        } else {
            holder.ivMedia.setVisibility(View.GONE);
        }

        if (tweet.retweeted) {
            holder.btRetweet.setBackgroundResource(R.drawable.ic_vector_retweet);
        } else {
            holder.btRetweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
        }

        if (tweet.favorited) {
            holder.btFavorite.setBackgroundResource(R.drawable.ic_vector_heart);
        } else {
            holder.btFavorite.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
        }

        holder.btReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComposeActivity.class);
                intent.putExtra("replyTo", tweet.user.screenName);
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
            }
        });
        holder.btRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (tweet.retweeted) {
                    client.postUnretweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "Tweet unretweeted!", Toast.LENGTH_SHORT).show();
                            v.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("TwitterClient", response.toString());

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });
                } else {
                    client.postRetweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "Tweet retweeted!", Toast.LENGTH_SHORT).show();
                            v.setBackgroundResource(R.drawable.ic_vector_retweet);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("TwitterClient", response.toString());

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });
                }
            }
        });
        holder.btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (tweet.favorited) {
                    client.destroyFavorite(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "Tweet unfavorited!", Toast.LENGTH_SHORT).show();
                            v.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("TwitterClient", response.toString());

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });
                } else {
                    client.postFavorite(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Toast.makeText(context, "Tweet favorited!", Toast.LENGTH_SHORT).show();
                            v.setBackgroundResource(R.drawable.ic_vector_heart);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("TwitterClient", response.toString());

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("TwitterClient", responseString);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("TwitterClient", errorResponse.toString());
                            throwable.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public ImageView ivMedia;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public TextView tvScreenName;
        public Button btReply;
        public Button btRetweet;
        public Button btFavorite;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvBody = itemView.findViewById(R.id.tvBody);
            btReply = itemView.findViewById(R.id.btReply);
            btRetweet = itemView.findViewById(R.id.btRetweet);
            btFavorite = itemView.findViewById(R.id.btFavorite);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position
                Tweet tweet = mTweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, DetailActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

}
