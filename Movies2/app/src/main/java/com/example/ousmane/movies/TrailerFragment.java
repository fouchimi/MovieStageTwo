package com.example.ousmane.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ousmane.movies.adapters.TrailerAdapter;
import com.example.ousmane.movies.entities.Constants;
import com.example.ousmane.movies.entities.Movie;
import com.example.ousmane.movies.entities.Trailer;
import com.example.ousmane.movies.network.HttpManager;
import com.example.ousmane.movies.parsers.MovieTrailersAndReviewsJSONParser;

import java.util.ArrayList;
import java.util.List;


public class TrailerFragment extends Fragment {
    private static final String LOG_TAG = TrailerFragment.class.getSimpleName();

    private Movie mMovie;
    private ListView mTrailerListView;
    private TrailerAdapter mTrailerAdapter;
    private List<Trailer> mTrailerList = new ArrayList<>();

    private Trailer mTrailer;

    public TrailerFragment() {

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
        View rootView = inflater.inflate(R.layout.fragment_trailer, container, false);
        mTrailerListView = (ListView) rootView.findViewById(R.id.trailer_listview);
            mTrailerAdapter = new TrailerAdapter(getActivity(), R.layout.trailer, mTrailerList);
            mTrailerListView.setAdapter(mTrailerAdapter);
        mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mTrailer = mTrailerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri youtubeUrl = Uri.parse("http://www.youtube.com/watch?v=" + mTrailer.getKey());
                intent.setData(youtubeUrl);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "Couldn't call " + youtubeUrl + ", no receiving apps installed!");
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Fetch trailers here.
        if(mMovie != null) {
            String trailerUrl = "https://api.themoviedb.org/3/movie/"+mMovie.getMovieId()+"/videos?api_key=" + Constants.API_KEY.getValue();
            new FetchMovieTrailer().execute(trailerUrl);
        }
    }

     public class FetchMovieTrailer extends AsyncTask<String, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(String... params) {
            String jsonData = new HttpManager().getData(params[0]);
            mTrailerList = new MovieTrailersAndReviewsJSONParser().parseMovieTrailers(jsonData);
            return mTrailerList;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if(trailers != null) {
                mTrailerList = trailers;
                mTrailerAdapter.clear();
                if(mTrailerList.size() > 2) {
                    for(int i=0; i < 2; i++) {
                        mTrailerAdapter.add(mTrailerList.get(i));
                    }
                }else {
                    for(Trailer trailer : mTrailerList) {
                        mTrailerAdapter.add(trailer);
                    }
                }
                mTrailerAdapter.notifyDataSetChanged();
            }

        }
    }
}
