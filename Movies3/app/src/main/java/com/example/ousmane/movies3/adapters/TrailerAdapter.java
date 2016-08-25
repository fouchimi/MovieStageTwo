package com.example.ousmane.movies3.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ousmane.movies3.R;
import com.example.ousmane.movies3.entities.Trailer;

import java.util.List;

/**
 * Created by ousmane on 8/20/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    private List<Trailer> mReviewList;
    private Context mContext;

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        this.mReviewList = trailers;
        this.mContext = context;
    }

    protected class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView trailer_id;
        TextView trailer_name;
        ImageView trailer_icon;

        public TrailerViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_view, parent, false));
            trailer_id = (TextView) itemView.findViewById(R.id.trailer_id);
            trailer_name = (TextView) itemView.findViewById(R.id.trailer_name);
            trailer_icon = (ImageView) itemView.findViewById(R.id.trailer_icon);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition() - 1;
                    Trailer trailer = TrailerAdapter.this.mReviewList.get(index);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri youtubeUrl = Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey());
                    intent.setData(youtubeUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (intent.resolveActivity(TrailerAdapter.this.mContext.getPackageManager()) != null) {
                        TrailerAdapter.this.mContext.startActivity(intent);
                    } else {
                        Log.d(LOG_TAG, "Couldn't call " + youtubeUrl + ", no receiving apps installed!");
                    }
                }
            });
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.trailer_id.setText("Trailer: " + (position + 1));
        holder.trailer_name.setText(mReviewList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }
}
