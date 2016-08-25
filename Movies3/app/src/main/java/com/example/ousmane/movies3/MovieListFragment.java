package com.example.ousmane.movies3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ousmane.movies3.adapters.MovieAdapter;
import com.example.ousmane.movies3.entities.Constants;
import com.example.ousmane.movies3.entities.Movie;
import com.example.ousmane.movies3.network.HttpManager;
import com.example.ousmane.movies3.parsers.MovieJSONParser;

import java.util.ArrayList;
import java.util.List;


public class MovieListFragment extends Fragment {
    private MovieAdapter mMovieAdapter;
    private List<Movie> movies = new ArrayList<>();
    private Movie mMovie;
    private Callbacks activity;

    public MovieListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        final String baseUrl = "http://api.themoviedb.org/3/discover/movie?api_key=" + Constants.API_KEY.getValue();
        new FetchMovieTask().execute(baseUrl);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        mMovieAdapter = new MovieAdapter(getActivity(), R.id.image_thumbnail, movies);
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mMovie = mMovieAdapter.getItem(position);
                activity.onItemSelected(mMovie);
            }
        });
        return rootView;
    }

    public interface Callbacks {
        public void onItemSelected(Movie movie);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Callbacks) context;
    }


    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {
            String jsonData = new HttpManager().getData(params[0]);
            movies = new MovieJSONParser().parseData(jsonData);
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if(movies != null) {
                mMovieAdapter.clear();
                for(Movie movie : movies) {
                    mMovieAdapter.add(movie);
                }
            }
        }
    }

}
