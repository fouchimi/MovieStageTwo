package com.example.ousmane.movies;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ousmane.movies.data.MovieContract;
import com.example.ousmane.movies.entities.Constants;
import com.example.ousmane.movies.entities.Movie;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();

            /* TrailerFragment trailerFragment = new TrailerFragment();
            trailerFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.trailer_container, trailerFragment)
                    .commit();

            ReviewFragment reviewFragment = new ReviewFragment();
            reviewFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.review_container, reviewFragment)
                    .commit(); */
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(Constants.MOVIE_KEY.getValue(),
                getIntent().getExtras().getParcelable(Constants.MOVIE_KEY.getValue()));
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.favorite_option:
                boolean checked = false;
                Bundle bundle = getIntent().getExtras();
                mMovie = bundle.getParcelable(Constants.MOVIE_KEY.getValue());
                Toast.makeText(this, "Title: " + mMovie.getTitle(), Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, "MOVIE TITLE: " + mMovie.getTitle());
                if(mMovie != null) {
                    checked = isSelectedMovieInDb(mMovie);
                }
                if(checked){
                    Toast.makeText(this, "Movie is already in DB", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "Movie is not yet in DB", Toast.LENGTH_LONG).show();
                }
                //Check to see if movie is already add in DB
                //Otherwise add it in database
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isSelectedMovieInDb(Movie movie) {
        String movieId = "";
        Cursor movieCursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{},
                MovieContract.MovieEntry.MOVIE_ID + "=?",
                new String[]{movie.getMovieId()},
                null);
        if(movieCursor.moveToFirst()) {
            int movieIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID);
            movieId = movieCursor.getString(movieIndex);
        }

        return !movieId.isEmpty();
    }
}
