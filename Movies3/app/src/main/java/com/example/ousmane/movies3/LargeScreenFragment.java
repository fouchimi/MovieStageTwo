package com.example.ousmane.movies3;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ousmane.movies3.adapters.ReviewAdapter;
import com.example.ousmane.movies3.adapters.SelectedMovieAdapter;
import com.example.ousmane.movies3.adapters.TrailerAdapter;
import com.example.ousmane.movies3.entities.Constants;
import com.example.ousmane.movies3.entities.Movie;
import com.example.ousmane.movies3.entities.Review;
import com.example.ousmane.movies3.entities.Trailer;
import com.example.ousmane.movies3.network.HttpManager;
import com.example.ousmane.movies3.parsers.MovieTrailersAndReviewsJSONParser;

import java.util.ArrayList;
import java.util.List;

import su.j2e.rvjoiner.JoinableAdapter;
import su.j2e.rvjoiner.JoinableLayout;
import su.j2e.rvjoiner.RvJoiner;


public class LargeScreenFragment extends Fragment {
    private static final String LOG_TAG = LargeScreenFragment.class.getSimpleName();
    private Movie mMovie;

    private ArrayList<Movie> mFavoriteMovies = new ArrayList<>();

    private List<Review> mReviewList = new ArrayList<>();
    private List<Trailer> mTrailerList = new ArrayList<>();
    private RecyclerView rv;
    private RvJoiner rvJoiner;

    // Adapters definition
    private SelectedMovieAdapter mSelectedMovieAdapter;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private Callbacks activity;

    public LargeScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.fragment_large_screen, container, false);
        rv = (RecyclerView) view.findViewById(R.id.ls_detail_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    public interface Callbacks {
        void favoriteMovies(ArrayList<Movie> favorites);
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
                mTrailerAdapter = new TrailerAdapter(getActivity(), movie.getTrailerList());
                mReviewAdapter = new ReviewAdapter(movie.getReviewList());

                rvJoiner.add(new JoinableAdapter(mSelectedMovieAdapter));
                rvJoiner.add(new JoinableLayout(R.layout.trailer_header));
                rvJoiner.add(new JoinableAdapter(mTrailerAdapter));
                rvJoiner.add(new JoinableLayout(R.layout.review_header));
                rvJoiner.add(new JoinableAdapter(mReviewAdapter));

                //set join adapter to your RecyclerView
                rv.setAdapter(rvJoiner.getAdapter());
            }
        }
    }


}
