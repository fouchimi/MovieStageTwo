package com.example.ousmane.movies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.ousmane.movies.R;
import com.example.ousmane.movies.entities.Movie;
//import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ousmane on 8/17/16.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private Context mContext;
    private List<Movie> movieList;

    public MovieAdapter(Context context, int resource, List<Movie> movies) {
        super(context, resource, movies);
        this.mContext = context;
        this.movieList = movies;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        /*convertView = inflater.inflate(R.layout.image_item, parent, false);
        ImageView mImageView = (ImageView) convertView.findViewById(R.id.image_thumbnail);
        Movie movie = movieList.get(position);
        Picasso.with(mContext)
                .load(movie.getImage())
                .into(mImageView); */
        return convertView;
    }
}
