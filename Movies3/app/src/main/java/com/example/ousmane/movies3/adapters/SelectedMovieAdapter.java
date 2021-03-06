package com.example.ousmane.movies3.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ousmane.movies3.R;
import com.example.ousmane.movies3.entities.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by ousmane on 8/23/16.
 */
public class SelectedMovieAdapter extends RecyclerView.Adapter<SelectedMovieAdapter.SelectedMovieViewHolder> {

    private Movie mMovie;
    private Context mContext;

    public SelectedMovieAdapter(Context context, Movie movie) {
        this.mMovie = movie;
        this.mContext = context;
    }

    protected class SelectedMovieViewHolder extends RecyclerView.ViewHolder {
        TextView detail_title;
        TextView detail_year;
        TextView detail_rating;
        TextView detail_synopsis;
        ImageView detail_thumbnail;

        SelectedMovieViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_movie_view, parent, false));
            detail_title = (TextView) itemView.findViewById(R.id.detail_title);
            detail_rating = (TextView) itemView.findViewById(R.id.detail_rating);
            detail_year = (TextView) itemView.findViewById(R.id.detail_year);
            detail_synopsis = (TextView) itemView.findViewById(R.id.detail_synopsis);
            detail_thumbnail = (ImageView) itemView.findViewById(R.id.detail_thumbnail);

        }
    }

    @Override
    public SelectedMovieAdapter.SelectedMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedMovieViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(SelectedMovieAdapter.SelectedMovieViewHolder holder, int position) {
        holder.detail_title.setText(cleanTitle(mMovie.getTitle()));
        String formattedDate = "<b><i>Year: </i></b>" + getYear(mMovie.getDate());
        holder.detail_year.setText(Html.fromHtml(formattedDate));
        String formattedRating = "<b><i>Rating: </i></b>" + mMovie.getRating();
        holder.detail_rating.setText(Html.fromHtml(formattedRating));
        String formattedSynopsis = "<b><i>Synopsis: </i></b>" + stripDescription(mMovie.getSynopsis());
        holder.detail_synopsis.setText(Html.fromHtml(formattedSynopsis));
        Picasso.with(mContext)
                .load(mMovie.getImage())
                .into(holder.detail_thumbnail);
    }

    @Override
    public int getItemCount() {
        return 1;
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

    private String stripDescription(String content) {
        String [] words = content.split(" ");
        if(words.length > 60) {
            return content.substring(0, 60) + " ...";
        }
        return content;
    }
}
