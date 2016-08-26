package com.example.ousmane.movies3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ousmane.movies3.adapters.FavoriteAdapter;
import com.example.ousmane.movies3.entities.Constants;
import com.example.ousmane.movies3.entities.Movie;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private ArrayList<Movie> mMovieArrayList;
    RecyclerView rv;
    LinearLayoutManager llm;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if(savedInstanceState == null) {
            mMovieArrayList = b.getParcelableArrayList(Constants.FAVORITE_ITEMS.getValue());
        }else {
            mMovieArrayList = savedInstanceState.getParcelableArrayList(Constants.FAVORITE_ITEMS.getValue());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.FAVORITE_ITEMS.getValue(), mMovieArrayList);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        rv = (RecyclerView) view.findViewById(R.id.favorite_rv);
        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        FavoriteAdapter adapter = new FavoriteAdapter(getActivity(), mMovieArrayList);
        rv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
