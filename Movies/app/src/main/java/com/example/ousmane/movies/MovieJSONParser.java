package com.example.ousmane.movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ousmane on 8/17/16.
 */
public class MovieJSONParser {
    private Movie movie;
    private List<Movie> list = new ArrayList<>();
    private static final String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String THUMBNAIL_SIZE = "w780/";

    public List<Movie> parseData(String json) {
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONArray results = rootObject.getJSONArray("results");
            for(int i=0; i < results.length(); i++) {
                JSONObject eachObject = results.getJSONObject(i);
                movie = new Movie();
                movie.setMovieId(eachObject.getString("id"));
                movie.setTitle(eachObject.getString("original_title"));
                movie.setDate(eachObject.getString("release_date"));
                movie.setImage(THUMBNAIL_BASE_URL + THUMBNAIL_SIZE + eachObject.getString("poster_path"));
                movie.setSynopsis(eachObject.getString("overview"));
                movie.setRating(eachObject.getDouble("vote_average"));
                list.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}