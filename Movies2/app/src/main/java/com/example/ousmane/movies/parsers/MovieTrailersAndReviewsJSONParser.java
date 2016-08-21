package com.example.ousmane.movies.parsers;

import android.util.Log;

import com.example.ousmane.movies.entities.Review;
import com.example.ousmane.movies.entities.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ousmane on 8/20/16.
 */
public class MovieTrailersAndReviewsJSONParser {
    private static final String LOG_TAG = MovieTrailersAndReviewsJSONParser.class.getSimpleName();
    private Trailer trailer;
    private Review review;

    public List<Trailer> parseMovieTrailers(String json) {
        List<Trailer> trailerList  = new ArrayList<Trailer>();
        try {
            Log.d(LOG_TAG, json);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            for(int i=0; i < results.length(); i++) {
                JSONObject eachObject = results.getJSONObject(i);
                trailer = new Trailer();
                trailer.setId(eachObject.getString("id"));
                trailer.setKey(eachObject.getString("key"));
                trailer.setName(eachObject.getString("name"));
                trailer.setSite(eachObject.getString("site"));
                trailer.setType(eachObject.getString("type"));

                trailerList.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerList;
    }

    public List<Review> parseMovieReviews(String json) {
        List<Review> reviewList  = new ArrayList<Review>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            for(int i=0; i < results.length(); i++) {
                JSONObject eachObject = results.getJSONObject(i);
                review = new Review();
                review.setId(eachObject.getString("id"));
                review.setAuthor(eachObject.getString("author"));
                review.setContent(eachObject.getString("content"));
                review.setUrl(eachObject.getString("url"));

                reviewList.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }
}
