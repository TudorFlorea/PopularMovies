package com.example.tudor.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tudor on 04.03.2018.
 */

public class Movie implements Parcelable {

    private long mId;
    private String mTitle;
    private String mReleaseDate;
    private String mReleaseYear;
    private String mTitleAndYear;
    private String mPosterLink;
    private String mBackdropLink;
    private double mVoteAverage;
    private String mPlot;

    private final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private final String MOVIE_BACKDROP_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public Movie(long id, String title, String releaseDate, String posterPath, String backdropPath, double voteAverage, String plot) {
        this.mId = id;
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mReleaseYear = buildReleaseYear(releaseDate);
        this.mPosterLink = buildPosterPath(posterPath);
        this.mBackdropLink = buildBackdropImagePath(backdropPath);
        this.mVoteAverage = voteAverage;
        this.mPlot = plot;
        this.mTitleAndYear = buildTitleAndYear();
    }

    public String buildPosterPath(String path) {
        return MOVIE_POSTER_BASE_URL + path;
    }

    public String buildBackdropImagePath(String path) {
        return MOVIE_BACKDROP_IMAGE_BASE_URL + path;
    }

    public long getId() {
        return this.mId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getReleaseDate() {
        return this.mReleaseDate;
    }

    public String getPosterLink() {
        return this.mPosterLink;
    }

    public String getBackdropLink() {
        return this.mBackdropLink;
    }

    public String getReleaseYear() {
        return this.mReleaseYear;
    }

    public double getVoteAverage() {
        return this.mVoteAverage;
    }

    public String getPlot() {
        return this.mPlot;
    }

    public String getTitleAndYear() {
        return this.mTitleAndYear;
    }

    private String buildReleaseYear(String date) {
        return date.substring(0, 4);
    }

    private String buildTitleAndYear() {
        return this.mTitle + " (" + this.mReleaseYear + ")";
    }

    protected Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mReleaseYear = in.readString();
        mTitleAndYear = in.readString();
        mPosterLink = in.readString();
        mBackdropLink = in.readString();
        mVoteAverage = in.readDouble();
        mPlot = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mReleaseYear);
        dest.writeString(mTitleAndYear);
        dest.writeString(mPosterLink);
        dest.writeString(mBackdropLink);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mPlot);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}