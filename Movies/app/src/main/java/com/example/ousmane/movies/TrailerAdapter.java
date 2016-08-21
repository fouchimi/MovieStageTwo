package com.example.ousmane.movies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ousmane on 8/20/16.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private Context mContext;
    private List<Trailer> movieList;

    public TrailerAdapter(Context context, int resource, List<Trailer> trailers) {
        super(context, resource, trailers);
        this.mContext = context;
        this.movieList = trailers;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.trailer_item, parent, false);
        TextView mTextView = (TextView) convertView.findViewById(R.id.trailer_name);
        Trailer trailer = movieList.get(position);
        mTextView.setText(trailer.getName());
        return convertView;
    }
}
