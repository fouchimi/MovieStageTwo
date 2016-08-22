package com.example.ousmane.movies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ousmane.movies.R;
import com.example.ousmane.movies.entities.Review;

import java.util.List;

/**
 * Created by ousmane on 8/20/16.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {

    private Context mContext;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, int resource, List<Review> reviews) {
        super(context, resource, reviews);
        this.mContext = context;
        this.reviewList = reviews;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.review, parent, false);
        TextView mReviewContent = (TextView) convertView.findViewById(R.id.review_content);
        Review review = reviewList.get(position);
        mReviewContent.setText(review.getContent());
        return convertView;
    }
}
