package com.example.tudor.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by tudor on 04.03.2018.
 */

public class Movie implements Parcelable {

    private long mId;
    private long mDatabaseId;
    private String mTitle;
    private String mReleaseDate;
    private String mReleaseYear;
    private String mTitleAndYear;
    private String mPosterPath;
    private String mBackdropPath;
    private double mVoteAverage;
    private String mPlot;
    private long mVoteCount;
    private double mPopularity;
    private ArrayList<Review> mReviews;
    private ArrayList<Trailer> mTrailers;

    private final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private final String MOVIE_BACKDROP_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public Movie(long id, String title, String releaseDate, String posterPath, String backdropPath, double voteAverage, String plot, long voteCount, double popularity, ArrayList<Review> reviews, ArrayList<Trailer> trailers) {
        this.mId = id;
        this.mDatabaseId = -1;
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mReleaseYear = buildReleaseYear(releaseDate);
        this.mPosterPath = posterPath;
        this.mBackdropPath = backdropPath;
        this.mVoteAverage = voteAverage;
        this.mPlot = plot;
        this.mVoteCount = voteCount;
        this.mPopularity = popularity;
        this.mTitleAndYear = buildTitleAndYear();
        this.mReviews = reviews;
        this.mTrailers = trailers;
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

    public void setDatabaseId(long id) {
        this.mDatabaseId = id;
    }

    public long getDatabaseID() {
        return this.mDatabaseId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getReleaseDate() {
        return this.mReleaseDate;
    }

    public String getPosterPath() {
        return this.mPosterPath;
    }

    public String getPosterLink() {
        return this.buildPosterPath(this.mPosterPath);
    }

    public String getPosterName() {
        return this.mPosterPath.substring(1);
    }

    public String getBackdropPath() {
        return this.mBackdropPath;
    }

    public String getBackdropLink() {
        return this.buildBackdropImagePath(this.mBackdropPath);
    }

    public String getBackdropName() {
        return this.mBackdropPath.substring(1);
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

    public double getPopularity() {
        return this.mPopularity;
    }

    public double getRoundPopularity() {
        return Math.round(this.mPopularity);
    }

    public long getVoteCount() {
        return this.mVoteCount;
    }

    public String getTitleAndYear() {
        return this.mTitleAndYear;
    }

    public ArrayList<Review> getReviews() {
        return this.mReviews;
    }

    public ArrayList<Trailer> getTrailers() {
        return this.mTrailers;
    }

    private String buildReleaseYear(String date) {
        return date.substring(0, 4);
    }

    private String buildTitleAndYear() {
        return this.mTitle + " (" + this.mReleaseYear + ")";
    }

    protected Movie(Parcel in) {
        mId = in.readLong();
        mDatabaseId = in.readLong();
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mReleaseYear = in.readString();
        mTitleAndYear = in.readString();
        mPosterPath = in.readString();
        mBackdropPath = in.readString();
        mVoteAverage = in.readDouble();
        mPlot = in.readString();
        mVoteCount = in.readLong();
        mPopularity = in.readDouble();
        if (in.readByte() == 0x01) {
            mReviews = new ArrayList<Review>();
            in.readList(mReviews, Review.class.getClassLoader());
        } else {
            mReviews = null;
        }
        if (in.readByte() == 0x01) {
            mTrailers = new ArrayList<Trailer>();
            in.readList(mTrailers, Trailer.class.getClassLoader());
        } else {
            mTrailers = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mDatabaseId);
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mReleaseYear);
        dest.writeString(mTitleAndYear);
        dest.writeString(mPosterPath);
        dest.writeString(mBackdropPath);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mPlot);
        dest.writeLong(mVoteCount);
        dest.writeDouble(mPopularity);
        if (mReviews == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mReviews);
        }
        if (mTrailers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mTrailers);
        }
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