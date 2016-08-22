package com.example.ousmane.movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ousmane.movies.adapters.ReviewAdapter;
import com.example.ousmane.movies.entities.Constants;
import com.example.ousmane.movies.entities.Movie;
import com.example.ousmane.movies.entities.Review;
import com.example.ousmane.movies.network.HttpManager;
import com.example.ousmane.movies.parsers.MovieTrailersAndReviewsJSONParser;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {

    private Movie mMovie;
    private ListView mReviewListView;
    private ReviewAdapter mReviewAdapter;
    private List<Review> mReviewList = new ArrayList<>();

    public ReviewFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        mReviewListView = (ListView) rootView.findViewById(R.id.review_listview);
        mReviewAdapter = new ReviewAdapter(getActivity(), R.layout.trailer, mReviewList);
        mReviewListView.setAdapter(mReviewAdapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.getParcelable(Constants.MOVIE_KEY.getValue());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mMovie != null) {
            String reviewUrl = "https://api.themoviedb.org/3/movie/"+mMovie.getMovieId()+"/reviews?api_key=" + Constants.API_KEY.getValue();
            new FetchMovieReviews().execute(reviewUrl);
        }
    }

    public class FetchMovieReviews extends AsyncTask<String, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(String... params) {
            String jsonData = new HttpManager().getData(params[0]);
            mReviewList = new MovieTrailersAndReviewsJSONParser().parseMovieReviews(jsonData);
            return mReviewList;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            mReviewList = reviews;
            if(reviews != null){
                mReviewList = reviews;
                mReviewAdapter.clear();
                for(Review review : mReviewList) {
                    mReviewAdapter.add(review);
                }
                mReviewAdapter.notifyDataSetChanged();
            }
        }
    }

}
