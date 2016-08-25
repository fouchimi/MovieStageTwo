package com.example.ousmane.movies3;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import com.example.ousmane.movies3.data.MovieContract;
import com.example.ousmane.movies3.entities.Movie;

/**
 * Created by ousmane on 8/24/16.
 */
public class TestUtilities extends AndroidTestCase {

    static ContentValues getMoviesValues() {

        ContentValues moviesContentValues = new ContentValues();
        moviesContentValues.put(MovieContract.MovieEntry.MOVIE_ID, "125");
        moviesContentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "First Movie");
        moviesContentValues.put(MovieContract.MovieEntry.COLUMN_IMAGE, "first_image.jpg");
        moviesContentValues.put(MovieContract.MovieEntry.COLUMN_RATING, "5.9");
        moviesContentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, "Synopsis");
        moviesContentValues.put(MovieContract.MovieEntry.COLUMN_DATE, "2016");
        moviesContentValues.put(MovieContract.MovieEntry.COLUMN_VOTE, "12");
        moviesContentValues.put(MovieContract.MovieEntry.COLUMN_TRAILER, "First|Second|Three");
        moviesContentValues.put(MovieContract.MovieEntry.COLUMN_REVIEWS, "ReviewOne|ReviewTwo|ReviewThree");

        return moviesContentValues;
    }
}
