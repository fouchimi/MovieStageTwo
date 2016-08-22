package com.example.ousmane.movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ousmane.movies.adapters.TrailerAdapter;
import com.example.ousmane.movies.entities.Constants;
import com.example.ousmane.movies.entities.Movie;
import com.example.ousmane.movies.entities.Trailer;
import com.example.ousmane.movies.network.HttpManager;
import com.example.ousmane.movies.parsers.MovieTrailersAndReviewsJSONParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment {
    private Movie mMovie;
    TextView mTitleTextView;
    TextView mYearTextView;
    TextView mRatingTextView;
    TextView mSynopsisTextView;
    TextView mVoteTextView;
    ImageView mThumbnailImageView;
    ListView mTrailerListView;

    private List<Trailer> mTrailerList = new ArrayList<>();
    private TrailerAdapter mTrailerAdapter;

    public MovieDetailFragment() {

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
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        if(mMovie != null) {
            //mVoteTextView = (TextView) rootView.findViewById(R.id.detail_vote);

            mTitleTextView = (TextView) rootView.findViewById(R.id.detail_title);
            mTitleTextView.setText(mMovie.getTitle());

            mYearTextView = (TextView) rootView.findViewById(R.id.detail_year);
            mYearTextView.setText("Year: " + getYear(mMovie.getDate()));

            mRatingTextView = (TextView) rootView.findViewById(R.id.detail_rating);
            mRatingTextView.setText("Rating: " + mMovie.getRating() + "/10");

            mSynopsisTextView = (TextView) rootView.findViewById(R.id.detail_synopsis);
            mSynopsisTextView.setText("Description: " + mMovie.getSynopsis());

            mThumbnailImageView = (ImageView) rootView.findViewById(R.id.detail_thumbnail);
            Picasso.with(getActivity())
                    .load(mMovie.getImage())
                    .into(mThumbnailImageView);
        }
        return rootView;
    }

    private String getYear(String date) {
        return date.split("-")[0];
    }



}
