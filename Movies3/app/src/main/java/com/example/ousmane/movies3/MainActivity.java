package com.example.ousmane.movies3;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ousmane.movies3.data.MovieContract;
import com.example.ousmane.movies3.entities.Constants;
import com.example.ousmane.movies3.entities.Movie;
import com.example.ousmane.movies3.entities.Review;
import com.example.ousmane.movies3.entities.Trailer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieListFragment.Callbacks, LargeScreenFragment.Callbacks {

    private boolean isTwoPane = false;
    private Movie selectedMovie = new Movie();
    private ArrayList<Movie> mFavoriteMovies = new ArrayList<>();
    FavoritesFragment favoritesFragment;
    private final String FAVORITE_TAG = "favorite";
    private final String BACK_STATE_NAME = FavoritesFragment.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isOnline()) {
            alertDialog();
        }
        if(findViewById(R.id.movie_detail_container) != null) {
            isTwoPane = true;
        }
    }

    private boolean isOnline(){
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }else {
            return false;
        }
    }

    private void alertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Internet Connection not available !!!");
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isTwoPane) {
            getMenuInflater().inflate(R.menu.large_screen_menu, menu);
        }else {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MovieListFragment movieListFragment = (MovieListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_movie_list);
        MovieListFragment.FetchMovieTask fetchMovieTask = movieListFragment.new FetchMovieTask();
        String baseUrl = "";
        switch (id) {
            case R.id.popular_ation:
                baseUrl = "http://api.themoviedb.org/3/movie/popular?api_key=" + Constants.API_KEY.getValue();
                fetchMovieTask.execute(baseUrl);
                return true;
            case R.id.top_rated_ation:
                baseUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + Constants.API_KEY.getValue();
                fetchMovieTask.execute(baseUrl);
                return true;
            case R.id.favorite_option :
                if(isSelectedMovieInDb(selectedMovie)) {
                    Toast.makeText(this, "Already in your favorite list", Toast.LENGTH_LONG).show();
                }else {
                    addSelectedMovieInDb(selectedMovie);
                }
                return true;
            case R.id.favorite_list_option_option :
                if(hasFavoriteMovies()){
                    /*FragmentManager fm = getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.FAVORITE_ITEMS.getValue(), getFavoritesMovies());
                    favoritesFragment = new FavoritesFragment();
                    favoritesFragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.movie_detail_container, favoritesFragment)
                            .addToBackStack(BACK_STATE_NAME)
                            .commit();*/
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.MOVIE_KEY.getValue(), getFavoritesMovies());
                    Intent intent = new Intent(this, FavoriteActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "You don't have any favorite movies yet. Be sure to add one in your list", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onItemSelected(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MOVIE_KEY.getValue(), movie);
        selectedMovie = movie;
        if(isTwoPane) {
            LargeScreenFragment largeScreenFragment = new LargeScreenFragment();
            bundle.putBoolean(Constants.LARGE.getValue(), true);
            largeScreenFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, largeScreenFragment, FAVORITE_TAG)
                    .commit();
        }else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void favoriteMovies(ArrayList<Movie> favorites) {
        return;
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
        movieCursor.close();
        return !movieId.isEmpty();
    }

    private void addSelectedMovieInDb(Movie movie) {
        ContentValues moviesValues = new ContentValues();
        moviesValues.put(MovieContract.MovieEntry.MOVIE_ID, movie.getMovieId());
        moviesValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        moviesValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, movie.getImage());
        moviesValues.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getRating());
        moviesValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        moviesValues.put(MovieContract.MovieEntry.COLUMN_DATE, movie.getDate());
        moviesValues.put(MovieContract.MovieEntry.COLUMN_VOTE, movie.getVote());
        String trailers = "";
        for(Trailer trailer: movie.getTrailerList()) {
            trailers += trailer.getName() + "|";
        }
        String reviews = "";
        for(Review review: movie.getReviewList()) {
            reviews += review.getContent() + "|";
        }
        moviesValues.put(MovieContract.MovieEntry.COLUMN_TRAILER, trailers);
        moviesValues.put(MovieContract.MovieEntry.COLUMN_REVIEWS, reviews);

        Uri insertedUri = getContentResolver()
                .insert(MovieContract.MovieEntry.CONTENT_URI, moviesValues);
        ContentUris.parseId(insertedUri);
        Toast.makeText(this, "This movie has been added in your favorite list", Toast.LENGTH_LONG)
                .show();
    }

    private boolean hasFavoriteMovies() {
        boolean flag = false;
        Cursor favoritesCursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{},
                null,
                null,
                null);
        if(favoritesCursor.moveToNext()) {
            flag = true;
        }
        favoritesCursor.close();
        return flag;
    }

    private ArrayList<Movie> getFavoritesMovies() {

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
        favoritesCursor.close();

        return  mFavoriteMovies;
    }
}
