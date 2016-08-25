package com.example.ousmane.movies3;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.ousmane.movies3.adapters.FavoriteAdapter;
import com.example.ousmane.movies3.data.MovieContract;
import com.example.ousmane.movies3.entities.Constants;
import com.example.ousmane.movies3.entities.Movie;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private static final String LOG_TAG = FavoriteActivity.class.getSimpleName();
    private ArrayList<Movie> mMovieArrayList;
    RecyclerView rv;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(savedInstanceState == null) {
            Bundle b = getIntent().getExtras();
            mMovieArrayList = b.getParcelableArrayList(Constants.MOVIE_KEY.getValue());
        }else {
            mMovieArrayList = loadFavoritesMovies();
        }
        updateView(mMovieArrayList);
    }

    public void updateView(ArrayList<Movie> movies){
        rv = (RecyclerView)findViewById(R.id.favorite_rv);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        FavoriteAdapter adapter = new FavoriteAdapter(this, movies);
        rv.setAdapter(adapter);
    }

    private ArrayList<Movie> loadFavoritesMovies() {
        ArrayList<Movie> mFavoriteMovies = new ArrayList<>();

        Cursor favoritesCursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{},
                null,
                null,
                null);
        while(favoritesCursor.moveToNext()) {

            String  movieId = favoritesCursor.getString(favoritesCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID));
            String  title = favoritesCursor.getString(favoritesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            String  image = favoritesCursor.getString(favoritesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE));
            String  rating = favoritesCursor.getString(favoritesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
            String  synopsis = favoritesCursor.getString(favoritesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS));
            String  date = favoritesCursor.getString(favoritesCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE));

            Movie movie = new Movie();
            movie.setMovieId(movieId);
            movie.setTitle(title);
            movie.setImage(image);
            movie.setRating(Double.parseDouble(rating));
            movie.setSynopsis(synopsis);
            movie.setDate(date);
            mFavoriteMovies.add(movie);
        }

        return mFavoriteMovies;
    }
}
