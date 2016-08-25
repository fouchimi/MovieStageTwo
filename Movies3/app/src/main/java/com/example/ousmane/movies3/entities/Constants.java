package com.example.ousmane.movies3.entities;

/**
 * Created by ousmane on 8/17/16.
 */
public enum Constants {
    API_KEY("c76c51a7ca44eb1182055d5eb5a576e8"),
    MOVIE_KEY("MOVIE_KEY"),
    FAVORITE_ITEMS("FAVORITE_ITEMS"),
    LARGE("LARGE");

    private String value;

    private Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
