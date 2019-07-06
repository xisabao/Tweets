package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 20;

    public @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    public @BindView(R.id.ivMedia) ImageView ivMedia;
    public @BindView((R.id.tvUserName)) TextView tvUsername;
    public @BindView(R.id.tvBody) TextView tvBody;
    public @BindView(R.id.tvTimestamp) TextView tvTimestamp;
    public @BindView(R.id.tvScreenName) TextView tvScreenName;
    public @BindView(R.id.btReply) Button btReply;
    public @BindView(R.id.btRetweet) Button btRetweet;
    public @BindView(R.id.btFavorite) Button btFavorite;

    TwitterClient client;

    Tweet tweet;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        client = TwitterApp.getRestClient(this);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvUsername.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvTimestamp.setText(TweetAdapter.getRelativeTimeAgo(tweet.createdAt));

        Glide.with(this).load(tweet.user.profileImageUrl).bitmapTransform(new CropCircleTransformation(DetailActivity.this)).into(ivProfileImage);

        if (tweet.media) {
            ivMedia.setVisibility(View.VISIBLE);
            Glide.with(DetailActivity.this).load(tweet.media_url).bitmapTransform(new RoundedCornersTransformation(DetailActivity.this, 10, 0)).into(ivMedia);
        } else {
            ivMedia.setVisibility(View.GONE);
        }

        if (tweet.retweeted) {
            btRetweet.setBackgroundResource(R.drawable.ic_vector_retweet);
        } else {
            btRetweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
        }

        if (tweet.favorited) {
            btFavorite.setBackgroundResource(R.drawable.ic_vector_heart);
        } else {
            btFavorite.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
        }

        btReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ComposeActivity.class);
                intent.putExtra("replyTo", tweet.user.screenName);
                DetailActivity.this.startActivityForResult(intent, REQUEST_CODE);
            }
        });
        btRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (tweet.retweeted) {
                        client.postUnretweet(tweet.uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                v.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
                                tweet.retweeted = false;
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
                                v.setBackgroundResource(R.drawable.ic_vector_retweet);
                                tweet.retweeted = true;
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
        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick ( final View v){
                    if (tweet.favorited) {
                        client.destroyFavorite(tweet.uid, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                v.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
                                tweet.favorited = false;
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
                                v.setBackgroundResource(R.drawable.ic_vector_heart);
                                tweet.favorited = true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.tweetCompose) {
            composeTweet();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra(Tweet.class.getSimpleName()));
            Toast.makeText(this, "Tweet posted", Toast.LENGTH_SHORT).show();
        }
    }

    private void composeTweet() {
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
