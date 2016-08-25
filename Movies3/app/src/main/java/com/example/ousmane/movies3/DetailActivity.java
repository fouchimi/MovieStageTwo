package com.example.ousmane.movies3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import com.example.ousmane.movies3.adapters.ReviewAdapter;
import com.example.ousmane.movies3.adapters.SelectedMovieAdapter;
import com.example.ousmane.movies3.adapters.TrailerAdapter;
import com.example.ousmane.movies3.entities.Constants;
import com.example.ousmane.movies3.entities.Movie;
import com.example.ousmane.movies3.entities.Review;
import com.example.ousmane.movies3.entities.Trailer;

import java.util.ArrayList;
import java.util.List;

import su.j2e.rvjoiner.RvJoiner;

public class DetailActivity extends AppCompatActivity implements DetailFragment.Callbacks {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        mMovie = bundle.getParcelable(Constants.MOVIE_KEY.getValue());
        if(savedInstanceState == null) {
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, detailFragment)
                    .commit();
        }

    }

    @Override
    public void favoriteMovies(ArrayList<Movie> favorites) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.MOVIE_KEY.getValue(), favorites);
        Intent intent = new Intent(this, FavoriteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
