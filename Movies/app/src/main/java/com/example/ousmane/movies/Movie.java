package com.example.ousmane.movies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ousmane on 8/17/16.
 */
public class Movie implements Parcelable {

    private String movieId;
    private String title;
    private String image;
    private String synopsis;
    private double rating;
    private String date;
    private String vote;
    private List<Trailer> trailerList;
    private List<Review> reviewList;

    public Movie() {

    }

    public Movie(Parcel in){
        this.movieId = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.synopsis = in.readString();
        this.rating = in.readDouble();
        this.date = in.readString();
        this.vote = in.readString();
        this.trailerList = new ArrayList<Trailer>();
        in.readTypedList(this.trailerList,Trailer.CREATOR);
        this.reviewList = new ArrayList<Review>();
        in.readTypedList(this.reviewList, Review.CREATOR);
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public List<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.movieId);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.synopsis);
        dest.writeDouble(this.rating);
        dest.writeString(this.date);
        dest.writeString(this.vote);
        dest.writeList(this.trailerList);
        dest.writeList(this.reviewList);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
