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
    private String mPosterLink;
    private String mBackdropLink;
    private double mVoteAverage;
    private String mPlot;

    private final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    public Movie(long id, String title, String releaseDate, String posterPath, String backdropPath, double voteAverage, String plot) {
        this.mId = id;
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mPosterLink = buildImagePath(posterPath);
        this.mBackdropLink = buildImagePath(backdropPath);
        this.mVoteAverage = voteAverage;
        this.mPlot = plot;
    }

    public String buildImagePath(String path) {
        return MOVIE_IMAGE_BASE_URL + path;
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

    public double getVoteAverage() {
        return this.mVoteAverage;
    }

    public String getPlot() {
        return this.mPlot;
    }



    protected Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mReleaseDate = in.readString();
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
