package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {
    // list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public boolean retweeted;
    public boolean favorited;
    public boolean media;
    public String media_url;

    public Tweet() {}

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.media = false;

        JSONObject entities = jsonObject.getJSONObject("entities");
        if (entities.has("media")) {
            tweet.media_url = entities.getJSONArray("media").getJSONObject(0).getString("media_url_https");
            tweet.media = true;
        }
        return tweet;
    }

}
