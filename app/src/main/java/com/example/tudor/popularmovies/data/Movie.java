package com.example.tudor.popularmovies.data;

/**
 * Created by tudor on 04.03.2018.
 */

public class Movie {

    private long mId;
    private String mTitle;
    private String mReleaseDate;
    private String mPosterPath;
    private double mVoteAverage;
    private String mPlot;

    private final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    public Movie(long id, String title, String releaseDate, String posterPath, double voteAverage, String plot) {
        this.mId = id;
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mPosterPath = posterPath;
        this.mVoteAverage = voteAverage;
        this.mPlot = plot;
    }

    public String getImagePath() {
        return MOVIE_IMAGE_BASE_URL + getPoster();
    }

    private String getPoster() {
        return this.mPosterPath;
    }

}
