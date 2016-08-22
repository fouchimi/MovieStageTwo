package com.example.ousmane.movies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ousmane.movies.R;
import com.example.ousmane.movies.entities.Trailer;

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
        convertView = inflater.inflate(R.layout.trailer, parent, false);
        TextView mTrailerName = (TextView) convertView.findViewById(R.id.trailer_name);
        TextView mTrailerId = (TextView) convertView.findViewById(R.id.trailer_id);
        Trailer trailer = movieList.get(position);
        int index = position + 1;
        mTrailerName.setText(trailer.getName());
        mTrailerId.setText("Trailer: " + index);
        return convertView;
    }
}
