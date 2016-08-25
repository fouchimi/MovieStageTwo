package com.example.ousmane.movies3;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ousmane.movies3.adapters.ReviewAdapter;
import com.example.ousmane.movies3.adapters.SelectedMovieAdapter;
import com.example.ousmane.movies3.adapters.TrailerAdapter;
import com.example.ousmane.movies3.data.MovieContract;
import com.example.ousmane.movies3.entities.Constants;
import com.example.ousmane.movies3.entities.Movie;
import com.example.ousmane.movies3.entities.Review;
import com.example.ousmane.movies3.entities.Trailer;
import com.example.ousmane.movies3.network.HttpManager;
import com.example.ousmane.movies3.parsers.MovieTrailersAndReviewsJSONParser;

import java.util.ArrayList;
import java.util.List;

import su.j2e.rvjoiner.JoinableAdapter;
import su.j2e.rvjoiner.RvJoiner;


public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private Movie mMovie;
    private static final int FAVORITE_OPTION = 1;
    private static final int FAVORITE_LIST_OPTION = 2;
    private ArrayList<Movie> mFavoriteMovies = new ArrayList<>();

    private List<Review> mReviewList = new ArrayList<>();
    private List<Trailer> mTrailerList = new ArrayList<>();
    private RecyclerView rv;
    private RvJoiner rvJoiner;

    // Adapters definition
    private SelectedMovieAdapter mSelectedMovieAdapter;
    private TrailerAdapter mTraierAdapter;
    private ReviewAdapter mReviewAdapter;
    private Callbacks activity;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(Constants.MOVIE_KEY.getValue())) {
            mMovie = bundle.getParcelable(Constants.MOVIE_KEY.getValue());
        }else if(savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(Constants.MOVIE_KEY.getValue());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.getParcelable(Constants.MOVIE_KEY.getValue());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailfragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.detail_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(hasFavoriteMovies()) {
            createCustomMenu(menu);
        }else {
            inflater.inflate(R.menu.detail_list_menu, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    private void createCustomMenu(Menu menu) {
        menu.add(menu.NONE, FAVORITE_OPTION, 100, R.string.favorite_option);
        menu.add(menu.NONE, FAVORITE_LIST_OPTION, 101, R.string.favorite_list_option);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.favorite_option :
                boolean checked = false;
                if(mMovie != null) {
                    checked = isSelectedMovieInDb(mMovie);
                    if(!checked) {
                        addSelectedMovieInDb(mMovie);
                    }else {
                        Toast.makeText(getActivity(), "Already added in  your favorite list", Toast.LENGTH_LONG)
                                .show();
                    }
                }
                return true;
            case FAVORITE_OPTION :
                checked = isSelectedMovieInDb(mMovie);
                if(!checked) {
                    addSelectedMovieInDb(mMovie);
                }else {
                    Toast.makeText(getActivity(), "Already added in  your favorite list", Toast.LENGTH_LONG)
                            .show();
                }
                return true;
            case FAVORITE_LIST_OPTION :
                viewFavoritesMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mMovie != null) {
            String  trailerUrl = "https://api.themoviedb.org/3/movie/"+mMovie.getMovieId()+"/videos?api_key=" + Constants.API_KEY.getValue();
            String reviewUrl = "https://api.themoviedb.org/3/movie/"+mMovie.getMovieId()+"/reviews?api_key=" + Constants.API_KEY.getValue();
            new FetchTrailersAndReviews().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, trailerUrl, reviewUrl);
        }
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

        Uri insertedUri = getActivity().getContentResolver()
                .insert(MovieContract.MovieEntry.CONTENT_URI, moviesValues);
        ContentUris.parseId(insertedUri);
        Toast.makeText(getActivity(), "This movie has been added in your favorite list", Toast.LENGTH_LONG)
                .show();
    }

    private boolean hasFavoriteMovies() {
        boolean flag = false;
        Cursor favoritesCursor = getActivity().getContentResolver().query(
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

    private void viewFavoritesMovies() {

        Cursor favoritesCursor = getActivity().getContentResolver().query(
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
        this.activity.favoriteMovies(mFavoriteMovies);
        favoritesCursor.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Callbacks) context;
    }

    public interface Callbacks {
        void favoriteMovies(ArrayList<Movie> favorites);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public boolean isSelectedMovieInDb(Movie movie) {
        String movieId = "";
        Cursor movieCursor = getActivity().getContentResolver().query(
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

    public class FetchTrailersAndReviews extends AsyncTask<String, Void, Movie> {

        @Override
        protected Movie doInBackground(String... params) {
            String jsonTrailerData = new HttpManager().getData(params[0]);
            mTrailerList = new MovieTrailersAndReviewsJSONParser().parseMovieTrailers(jsonTrailerData);
            mMovie.setTrailerList(mTrailerList);
            String jsonReviewData = new HttpManager().getData(params[1]);
            mReviewList = new MovieTrailersAndReviewsJSONParser().parseMovieReviews(jsonReviewData);
            mMovie.setReviewList(mReviewList);
            return mMovie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            if(movie != null) {
                //construct a joiner
                rvJoiner = new RvJoiner();

                mSelectedMovieAdapter = new SelectedMovieAdapter(getActivity(), movie);
                mTraierAdapter = new TrailerAdapter(getActivity(), movie.getTrailerList());
                mReviewAdapter = new ReviewAdapter(movie.getReviewList());

                rvJoiner.add(new JoinableAdapter(mSelectedMovieAdapter));
                rvJoiner.add(new JoinableAdapter(mTraierAdapter));
                rvJoiner.add(new JoinableAdapter(mReviewAdapter));

                //set join adapter to your RecyclerView
                rv.setAdapter(rvJoiner.getAdapter());
            }
        }
    }

}
