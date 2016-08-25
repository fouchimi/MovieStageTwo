package com.example.ousmane.movies3;

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
import com.example.ousmane.movies3.entities.Constants;
import com.example.ousmane.movies3.entities.Movie;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private static final String LOG_TAG = FavoriteActivity.class.getSimpleName();
    private ArrayList<Movie> mMovieArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(savedInstanceState == null) {
            Bundle b = getIntent().getExtras();
            mMovieArrayList = b.getParcelableArrayList(Constants.MOVIE_KEY.getValue());
        }else if(savedInstanceState != null) {
            mMovieArrayList = savedInstanceState.getParcelableArrayList(Constants.MOVIE_KEY.getValue());
        }
        RecyclerView rv = (RecyclerView)findViewById(R.id.favorite_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        FavoriteAdapter adapter = new FavoriteAdapter(this, mMovieArrayList);
        rv.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.getParcelableArrayList(Constants.MOVIE_KEY.getValue());
        super.onSaveInstanceState(outState);
    }

}
