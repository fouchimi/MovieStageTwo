package com.example.ousmane.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private TextView mTitleTextView;
    private TextView mYearTextView;
    private TextView mRatingTextView;
    private TextView mSynopsisTextView;
    private ImageView mThumbnailImageView;
    private TextView mVoteTextView;
    private ListView mTrailerListView;
    private ListView mReviewListView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private List<Trailer> mTrailerList = new ArrayList<>();
    private List<Review> mReviewList = new ArrayList<>();
    private Movie mMovie;
    private Trailer mTrailer;
    private Review mReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.detail_title);
        mYearTextView = (TextView) findViewById(R.id.detail_year);
        mRatingTextView = (TextView) findViewById(R.id.detail_rating);
        mSynopsisTextView = (TextView) findViewById(R.id.detail_synopsis);
        mThumbnailImageView = (ImageView) findViewById(R.id.detail_thumbnail);
        mVoteTextView = (TextView) findViewById(R.id.detail_vote);

        if(findViewById(R.id.trailerContainer) != null) {
            mTrailerAdapter = new TrailerAdapter(getApplicationContext(), R.layout.trailer_listview, mTrailerList);
            mTrailerListView = (ListView) findViewById(R.id.trailer_listview);
            mTrailerListView.setAdapter(mTrailerAdapter);
            mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    mTrailer = mTrailerAdapter.getItem(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Toast.makeText(getApplicationContext(), "Key: " + mTrailer.getKey(), Toast.LENGTH_LONG).show();
                    Uri youtubeUrl = Uri.parse("http://www.youtube.com/watch?v=" + mTrailer.getKey());
                    intent.setData(youtubeUrl);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Log.d(LOG_TAG, "Couldn't call " + youtubeUrl + ", no receiving apps installed!");
                    }
                }
            });
        }

        if(findViewById(R.id.reviewContainer) != null) {
            mReviewAdapter = new ReviewAdapter(this, R.layout.review_listview, mReviewList);
            mReviewListView = (ListView) findViewById(R.id.review_listView);
            mReviewListView.setAdapter(mReviewAdapter);
            mReviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    mReview = mReviewAdapter.getItem(position);
                }
            });
        }

        Bundle b = getIntent().getExtras();
        if(b != null && b.containsKey(Constants.MOVIE_KEY.getValue())) {
            mMovie = b.getParcelable(Constants.MOVIE_KEY.getValue());
            mTitleTextView.setText(mMovie.getTitle());
            mYearTextView.setText("Year: " + getYear(mMovie.getDate()));
            mRatingTextView.setText("Rating: " + mMovie.getRating() + "/10");
            mSynopsisTextView.setText("Description: " + mMovie.getSynopsis());
            Picasso.with(this)
                    .load(mMovie.getImage())
                    .into(mThumbnailImageView);
        }else {
            mMovie = savedInstanceState.getParcelable(Constants.MOVIE_KEY.getValue());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(Constants.MOVIE_KEY.getValue(), mMovie);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private String getYear(String date) {
        return date.split("-")[0];
    }

    @Override
    public void onStart() {
        super.onStart();
        // Fetch reviews and trailers here.
        String trailerUrl = "https://api.themoviedb.org/3/movie/"+mMovie.getMovieId()+"/videos?api_key=" + Constants.API_KEY.getValue();
        new FetchMovieTrailer().execute(trailerUrl);
        String reviewUrl = "https://api.themoviedb.org/3/movie/"+mMovie.getMovieId()+"/reviews?api_key=" + Constants.API_KEY.getValue();
        new FetchMovieReviews().execute(reviewUrl);
    }

    public class FetchMovieTrailer extends AsyncTask<String, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(String... params) {
            String jsonData = new HttpManager().getData(params[0]);
            mTrailerList = new MovieTrailersAndReviewsJSONParser().parseMovieTrailers(jsonData);
            return mTrailerList;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if(trailers != null) {
                mTrailerList = trailers;
                mTrailerAdapter.clear();
                for(Trailer trailer : mTrailerList) {
                    mTrailerAdapter.add(trailer);
                }
                mTrailerAdapter.notifyDataSetChanged();
            }

        }
    }

    public class FetchMovieReviews extends AsyncTask<String, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(String... params) {
            String jsonData = new HttpManager().getData(params[0]);
            mReviewList = new MovieTrailersAndReviewsJSONParser().parseMovieReviews(jsonData);
            return mReviewList;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            mReviewList = reviews;
            if(reviews != null){
                mReviewList = reviews;
                mReviewAdapter.clear();
                for(Review review : mReviewList) {
                    mReviewAdapter.add(review);
                }
                mReviewAdapter.notifyDataSetChanged();
            }
        }
    }

}
