package com.example.ousmane.movies3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ousmane.movies3.R;
import com.example.ousmane.movies3.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ousmane on 8/24/16.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private static final String LOG_TAG = FavoriteAdapter.class.getSimpleName();

    private ArrayList<Movie> mMovies;
    private Context mContext;

    public FavoriteAdapter(Context context, ArrayList<Movie> movies){
        this.mContext = context;
        this.mMovies = movies;
    }

    protected class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private TextView favorite_title;
        private TextView favorite_rating;
        private TextView favorite_year;
        private TextView favorite_desc;
        private ImageView favorite_thumbnail;

        public FavoriteViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_view, parent, false));
            favorite_title = (TextView) itemView.findViewById(R.id.favorite_title);
            favorite_rating = (TextView) itemView.findViewById(R.id.favorite_rating);
            favorite_year = (TextView) itemView.findViewById(R.id.favorite_year);
            favorite_desc = (TextView) itemView.findViewById(R.id.favorite_desc);
            favorite_thumbnail = (ImageView) itemView.findViewById(R.id.favorite_thumbnail);
        }
    }

    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.FavoriteViewHolder holder, int position) {
        String formattedTitle = "<b><i>Title: </i></b>" + cleanTitle(mMovies.get(position).getTitle());
        holder.favorite_title.setText(Html.fromHtml(formattedTitle));
        String formattedRating = "<b><i>Rating: </i></b>" + mMovies.get(position).getRating();
        holder.favorite_rating.setText(Html.fromHtml(formattedRating));
        String formattedYear = "<b><i>Year: </i></b>" + getYear(mMovies.get(position).getDate());
        holder.favorite_year.setText(Html.fromHtml(formattedYear));
        String formattedSynopsis = "<b><i>Synopsis: </i></b>" + mMovies.get(position).getSynopsis();
        holder.favorite_desc.setText(Html.fromHtml(formattedSynopsis));
        Log.d(LOG_TAG, mMovies.get(position).getTitle());
        Picasso.with(mContext)
                .load(mMovies.get(position).getImage())
                .into(holder.favorite_thumbnail);
    }

    private String cleanTitle(String title) {
        char ch = ':';
        String newTitle = "";
        if(title.indexOf(ch) > 0) {
            newTitle = title.split(":")[0];
            return newTitle;
        }
        return title;
    }

    private String getYear(String date) {
        return date.split("-")[0];
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}
