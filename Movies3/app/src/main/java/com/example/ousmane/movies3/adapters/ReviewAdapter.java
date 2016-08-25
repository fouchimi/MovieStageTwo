package com.example.ousmane.movies3.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ousmane.movies3.R;
import com.example.ousmane.movies3.entities.Review;

import java.util.List;

/**
 * Created by ousmane on 8/20/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> mReviewList;

    public ReviewAdapter(List<Review> reviews) {
        this.mReviewList = reviews;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView review_content;

        public ReviewViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_view, parent, false));
            review_content = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        holder.review_content.setText(stripContent(mReviewList.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    private String stripContent(String content) {
        String [] words = content.split(" ");
        if(words.length > 200) {
            return content.substring(0, 200) + " ...";
        }
        return content;
    }
}
